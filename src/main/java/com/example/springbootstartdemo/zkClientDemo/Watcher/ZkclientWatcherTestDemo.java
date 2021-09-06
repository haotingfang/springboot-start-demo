package com.example.springbootstartdemo.zkClientDemo.Watcher;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

public class ZkclientWatcherTestDemo  implements Watcher {

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
        System.out.println("eventType:"+watchedEvent.getType());
        if(watchedEvent.getType()==Event.EventType.NodeDataChanged){//持久监听
            try {
                zooKeeper.exists(watchedEvent.getPath(),true);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //getData()/  exists  /getChildren
    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        //Curator
        String path="/watcher";
        if(zooKeeper.exists(path,false)==null) {
            zooKeeper.create("/watcher", "0".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        Thread.sleep(1000);
        System.out.println("-----------");
//        Stat stat=zooKeeper.exists(path,true); //true表示使用zookeeper实例中配置的watcher 该例子中也就是使用ZkclientWatcherTestDemo.process()监听
        Stat stat=zooKeeper.exists(path,new Test()); //此时会使用Test.process()监听

        System.in.read();
    }
}
