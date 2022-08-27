package com.usian.sms.utils;

import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * 发送163邮件
 * @author shenwang
 */
@Log4j2
public class MailUtils {
    /**
     * 日志记录
     */
    private static Logger logger = LoggerFactory.getLogger(MailUtils.class);
    /**
     * 发件人邮箱
     */
    private static final String HOST="cqmm60@163.com";
    /**
     * 邮箱密码或授权码
     */
    private static final String PASSWORD="JHVKUDCWFNVOKSYU";
 
    /**
     * 发送邮件
     * @param direction 邮件人邮箱地址
     * @param subject 邮件名称/标题
     * @param message 消息、内容
     */
    public static boolean sendMail(String direction,String subject,String message){
 
        Properties props = new Properties();
        // 开启debug调试
        props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", "smtp.163.com");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");
 
        Session session = Session.getInstance(props);
        Transport transport= null;
        //新建消息
        Message msg = new MimeMessage(session);
        try {
            //设置邮件标题
            msg.setSubject(subject);
           //设置消息内容
            msg.setText(message);
            msg.setFrom(new InternetAddress(HOST));
            transport = session.getTransport();
            transport.connect("smtp.163.com", HOST, PASSWORD);
            transport.sendMessage(msg, new Address[] { new InternetAddress(direction) });
 
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }finally {
            try {
                //关闭、释放资源
                transport.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
 
 
}