package com.dqpi.wishList.util;

import com.dqpi.wishList.model.User;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class App {
    //全局线程池
    public static final ThreadPoolExecutor mThreadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    public static User localUser;
}
