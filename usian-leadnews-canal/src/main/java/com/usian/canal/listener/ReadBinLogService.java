package com.usian.canal.listener;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.List;

@Component
public class ReadBinLogService implements ApplicationRunner {

    @Value("${canal.host}")
    private String host;

    @Value("${canal.port}")
    private int port;
    @Autowired
    private RabbitTemplate rabbitTemplate;



    /**
     * 自动运行  不用动
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        CanalConnector conn = getConn();
        while (true) {
            conn.connect();
            //订阅实例中所有的数据库和表
            conn.subscribe(".*\\..*");
            // 回滚到未进行ack的地方
            conn.rollback();
            // 获取数据 每次获取一百条改变数据
            Message message = conn.getWithoutAck(100);

            long id = message.getId();
            int size = message.getEntries().size();
            if (id != -1 && size > 0) {
                // 数据解析
                analysis(message.getEntries());
            }else {
                Thread.sleep(1000);
            }
            // 确认消息
            conn.ack(message.getId());
            // 关闭连接
            conn.disconnect();
        }
    }

    /**
     * 数据解析 不要动
     */
    private void analysis(List<CanalEntry.Entry> entries) {
        for (CanalEntry.Entry entry : entries) {
            // 只解析mysql事务的操作，其他的不解析
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN) {
                continue;
            }
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                continue;
            }
            // 解析binlog
            CanalEntry.RowChange rowChange = null;
            try {
                rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("解析出现异常 data:" + entry.toString(), e);
            }
            if (rowChange != null) {
                // 获取操作类型
                CanalEntry.EventType eventType = rowChange.getEventType();
                // 获取当前操作所属的数据库
                String dbName = entry.getHeader().getSchemaName();
                // 获取当前操作所属的表
                String tableName = entry.getHeader().getTableName();
                // 事务提交时间
                long timestamp = entry.getHeader().getExecuteTime();
                for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
                    dataDetails(rowData.getBeforeColumnsList(), rowData.getAfterColumnsList(), dbName, tableName, eventType, timestamp);
                    System.out.println("-------------------------------------------------------------");
                }
            }
        }
    }


    /**
     * 解析具体一条Binlog消息的数据
     * 根据自己业务修改修改对应代码
     * @param dbName    当前操作所属数据库名称
     * @param tableName 当前操作所属表名称
     * @param eventType 当前操作类型（新增、修改、删除）
     */
    private  void dataDetails(List<CanalEntry.Column> beforeColumns,
                              List<CanalEntry.Column> afterColumns,
                              String dbName,
                              String tableName,
                              CanalEntry.EventType eventType,
                              long timestamp) {

        System.out.println("数据库：" + dbName);
        System.out.println("表名：" + tableName);
        System.out.println("操作类型:" + eventType);
        //这边通过解析获取到具体的操作然后来实现自动实名审核
        if(dbName.equals("leadnews_user")&&tableName.equals("ap_user_realname")&&CanalEntry.EventType.INSERT.equals(eventType)){
            //当这三个都满足 才会给MQ发送消息 然后实现自动审核
            System.out.println("开始审核");
            //获取添加返回的id 主键
            String id =  printColumn(afterColumns);
            System.out.println(id);
            //给MQ发送消息
            rabbitTemplate.convertAndSend("authExchange","auth",id);
        }
        if(dbName.equals("leadnews_article")&&tableName.equals("ap_article")&&CanalEntry.EventType.INSERT.equals(eventType)){
            System.out.println("可以进行增量索引更新，提取数据发送消息");
            //获取添加返回的id 主键
            String id =  printColumn(afterColumns);
            System.out.println(id);
            rabbitTemplate.convertAndSend("ESExchange","es",id);
        }

        // if (CanalEntry.EventType.INSERT.equals(eventType)) {
        //     System.out.println("新增数据：");
        //     String id =  printColumn(afterColumns);
        //     if(tableName.equals("ap_user_realname")){
        //         // 发送MQ（--》 自动审核）
        //         // 发送新增这条数据的id
        //         String message = id;
        //     }
        // } else if (CanalEntry.EventType.DELETE.equals(eventType)) {
        //     System.out.println("删除数据：");
        //     printColumn(beforeColumns);
        // } else {
        //     System.out.println("更新数据：更新前数据--");
        //     printColumn(beforeColumns);
        //     System.out.println("更新数据：更新后数据--");
        //     printColumn(afterColumns);
        // }
        System.out.println("操作时间：" + timestamp);
    }

    /**
     * 自己写的
     * @param columns
     * @return
     */
    private String printColumn(List<CanalEntry.Column> columns) {
        String id = "";
        for (CanalEntry.Column column : columns) {
            //if(column.getUpdated())
            System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
            //判断 如果是添加 或者是更新 获取afterlist
            if (column.getName().equalsIgnoreCase("id")) {
                id = column.getValue();
                return id;
            }
        }
        return id;
    }

    /**
     * 获取连接 根据自己参数进行修改即可
     */
    public CanalConnector getConn() {
        return CanalConnectors.newSingleConnector(new InetSocketAddress(host, port), "example", "", "");
    }
}
