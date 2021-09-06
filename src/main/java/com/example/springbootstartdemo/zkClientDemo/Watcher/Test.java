package com.example.springbootstartdemo.zkClientDemo.Watcher;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class Test implements Watcher {
    static ZooKeeper zooKeeper;

    static {
        try {
            zooKeeper = new ZooKeeper("localhost:2182,localhost:2183,localhost:2184", 4000,new ZkclientWatcherTestDemo());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("----------Test-----eventType:"+watchedEvent.getType());
        if(watchedEvent.getType()==Event.EventType.NodeDataChanged){//持久监听
            try {
                zooKeeper.exists(watchedEvent.getPath(),new Test());
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
