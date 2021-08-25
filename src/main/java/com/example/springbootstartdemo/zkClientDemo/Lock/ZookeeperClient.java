package com.example.springbootstartdemo.zkClientDemo.Lock;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZookeeperClient {

    //zk集群地址
    public static final String ZOOKEEPER_CONNECT="127.0.0.1:2182,127.0.0.1:2183,127.0.0.1:2184";
    //计数器，用于等待连接成功
    public static CountDownLatch countDownLatch = new CountDownLatch(1);
    //连接超时时间
    public static final int SESSION_TIMEOUT = 5000;
    //用volatile修饰单例，防止赋值时发生指令重排
    private volatile static ZooKeeper instance;
    //用Double check获取单例
    public static ZooKeeper getInstance() throws IOException, InterruptedException {
        if (instance == null ){
            synchronized (ZookeeperClient.class) {
                if (instance == null) {
                    //连接时注册一个监听，监听连接状态变化
                    instance = new ZooKeeper(ZOOKEEPER_CONNECT, SESSION_TIMEOUT, new Watcher() {
                        //监听回调方法
                        @Override
                        public void process(WatchedEvent watchedEvent) {
                            //当连接状态变成connected，就说明连接成功
                            if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                                countDownLatch.countDown();
                            }
                        }
                    });
                    //等待连接成功
                    countDownLatch.await();
                }
            }
        }
        return instance;
    }

    public static int getSessionTimeout() {
        return SESSION_TIMEOUT;
    }


}
