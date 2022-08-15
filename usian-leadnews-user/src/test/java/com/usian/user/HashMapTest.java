package com.usian.user;

import org.junit.Test;

import java.util.HashMap;

/**
 * @program: usian-leadnews
 * @description: HashMapTest
 * @author: wangheng
 * @create: 2022-08-14 18:54
 **/
public class HashMapTest {
    @Test
    public void aa(){
        HashMap map=new HashMap();
        map.put(1,1);
        HashMap hashMap=new HashMap(map);
        System.out.println(hashMap);
    }
}
