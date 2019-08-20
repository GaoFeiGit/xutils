package com.wxzd.fts.project.webservice.response;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SoapThreadPool {

    private volatile  static ExecutorService executor;

    private SoapThreadPool(){}

    public static ExecutorService getInstance(){

        if(executor==null){
            synchronized (SoapThreadPool.class){
                if(executor==null){
                    executor = Executors.newCachedThreadPool();
                }
            }
        }
        return executor;
    }


}
