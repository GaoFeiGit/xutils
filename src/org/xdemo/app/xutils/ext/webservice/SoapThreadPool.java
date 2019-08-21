package org.xdemo.app.xutils.ext.webservice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class SoapThreadPool {

    private volatile  static ExecutorService executor;

    private SoapThreadPool(){}

    static ExecutorService getInstance(){

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
