package com.example.springbootstartdemo.zkClientDemo.Lock;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DistibutedLock {

    //根目录，客户端都会去此目录下创建临时有序子节点
    private final String ROOT_PATH = "/lock";
    //客户端
    private ZooKeeper zookeeper;
    //session超时时间
    private  int SESSION_TIMEOUT;
    //当前客户端创建有序节点的名称
    private String lockId;

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public DistibutedLock() throws InterruptedException, IOException {
        this.zookeeper =ZookeeperClient.getInstance();
        this.SESSION_TIMEOUT = ZookeeperClient.getSessionTimeout();
    }

    public boolean lock(){

        try {
            //创建临时有序子节点
            lockId = zookeeper.create(ROOT_PATH+"/","123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

            System.out.println(Thread.currentThread().getName()+"创建节点"+lockId+",开始竞争锁");
            //获取/lock目录下所有子节点
            List<String> children = zookeeper.getChildren(ROOT_PATH, true);
            //用SortedSet对子节点从小到大进行排序
            SortedSet<String> sortedSet = new TreeSet<String>();
            for (String child : children) {
                sortedSet.add(ROOT_PATH+"/"+child);
            }
            //获取最小节点名称
            String first = sortedSet.first();
            //如果当前创建节点就是最小节点，则获取锁
            if (first.equals(lockId)) {
                System.out.println(Thread.currentThread().getName()+"获取锁"+lockId);
                return true;
            }
            //获取比当前id小的节点集合
            SortedSet<String> frontSet = sortedSet.headSet(lockId);
            if (!frontSet.isEmpty()) {
                //取集合中最后一个元素,也就是临近最小节点
                String last = frontSet.last();
                System.out.println(lockId+"监听"+last);
                //当前节点去监听上一个节点，当上一个节点被删除的时候
                //当前节点就可以获取锁
                zookeeper.exists(last, new Watcher() {
                    @Override
                    public void process(WatchedEvent watchedEvent) {
                        if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
                            countDownLatch.countDown();
                        }
                    }
                });
                countDownLatch.await(SESSION_TIMEOUT, TimeUnit.MILLISECONDS);
                System.out.println(Thread.currentThread().getName() + "获取锁" + lockId);
            }
            return true;

        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    //释放锁
    public boolean unLock(){
        try {
            System.out.println(Thread.currentThread().getName() + "开始删除锁" + lockId);
            //删除当前节点
            zookeeper.delete(lockId, -1);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
        return false;
    }
}
