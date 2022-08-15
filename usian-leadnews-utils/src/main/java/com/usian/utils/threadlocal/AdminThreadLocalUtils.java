package com.usian.utils.threadlocal;


import com.usian.model.admin.pojos.AdUser;

import java.util.ArrayList;

public class AdminThreadLocalUtils {

    private final  static ThreadLocal<AdUser> userThreadLocal = new ThreadLocal<>();

    /**
     * 设置当前线程中的用户
     * @param user
     */
    public static void setUser(AdUser user){
        userThreadLocal.set(user);
    }

    /**
     * 获取线程中的用户
     * @return
     */
    public static AdUser getUser( ){
        return userThreadLocal.get();
    }

}
