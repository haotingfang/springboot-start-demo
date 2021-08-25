package com.example.springbootstartdemo.zkClientDemo.Lock;

import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

public class ZkClientLockTestDemo {

    public static void main(String[] args) throws IOException, InterruptedException {


        //等待器，当所有线程都执行到某个步骤才停止阻塞
        CyclicBarrier cyclicBarrier = new CyclicBarrier(10);
        //模拟十个线程去获取锁
        for (int i = 0; i < 10; i++) {
            new Thread(()-> {
                DistibutedLock lock = null;
                try {
                    lock = new DistibutedLock();
                    cyclicBarrier.await();
                    lock.lock();
                    TimeUnit.MILLISECONDS.sleep(new Random().nextInt(500));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                } finally {
                    if(lock!=null){
                        lock.unLock();
                    }
                }
            }).start();
        }
    }


}
