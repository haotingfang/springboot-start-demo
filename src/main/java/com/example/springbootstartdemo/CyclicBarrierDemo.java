package com.example.springbootstartdemo;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {

    /*
    * CyclicBarrier用法
    * 通过它可以实现让一组线程等待至某个状态之后再全部同时执行
    * CyclicBarrier提供2个构造器：
    * public CyclicBarrier(int parties, Runnable barrierAction) {}
    * public CyclicBarrier(int parties) {}
    * 参数parties指让多少个线程或者任务等待至barrier状态；参数barrierAction为当这些线程都达到barrier状态时会执行的内容。
    *
    * 然后CyclicBarrier中最重要的方法就是await方法，它有2个重载版本：
    * public int await() throws InterruptedException, BrokenBarrierException { };
    * public int await(long timeout, TimeUnit unit)throws InterruptedException,BrokenBarrierException,TimeoutException { };
    * 第一个版本比较常用，用来挂起当前线程，直至所有线程都到达barrier状态再同时执行后续任务；
    * 第二个版本是让这些线程等待至一定的时间，如果还有线程没有到达barrier状态就直接让到达barrier的线程执行后续任务。
    * */

    public static void main(String[] args) {
        test1();
//        test2();
    }

    /*
    * 线程Thread-0正在写入数据...
    * 线程Thread-3正在写入数据...
    * 线程Thread-2正在写入数据...
    * 线程Thread-1正在写入数据...
    * 线程Thread-2写入数据完毕，等待其他线程写入完毕
    * 线程Thread-0写入数据完毕，等待其他线程写入完毕
    * 线程Thread-3写入数据完毕，等待其他线程写入完毕
    * 线程Thread-1写入数据完毕，等待其他线程写入完毕
    * 所有线程写入完毕，继续处理其他任务...
    * 所有线程写入完毕，继续处理其他任务...
    * 所有线程写入完毕，继续处理其他任务...
    * 所有线程写入完毕，继续处理其他任务...*/
    public static  void test1(){
        int N = 4;
        CyclicBarrier barrier  = new CyclicBarrier(N);
        for(int i=0;i<N;i++)
            new Writer(barrier).start();
    }

    /*
    * 线程Thread-0正在写入数据...
    * 线程Thread-1正在写入数据...
    * 线程Thread-2正在写入数据...
    * 线程Thread-3正在写入数据...
    * 线程Thread-0写入数据完毕，等待其他线程写入完毕
    * 线程Thread-1写入数据完毕，等待其他线程写入完毕
    * 线程Thread-2写入数据完毕，等待其他线程写入完毕
    * 线程Thread-3写入数据完毕，等待其他线程写入完毕
    * 当前线程Thread-3
    * 所有线程写入完毕，继续处理其他任务...
    * 所有线程写入完毕，继续处理其他任务...
    * 所有线程写入完毕，继续处理其他任务...
    * 所有线程写入完毕，继续处理其他任务...
    *
    * 注：从结果可以看出，当四个线程都到达barrier状态后，会从四个线程中选择一个线程去执行Runnable。
    * */
    public static  void test2(){
        int N = 4;
        CyclicBarrier barrier  = new CyclicBarrier(N,new Runnable() {
            @Override
            public void run() {
                System.out.println("当前线程"+Thread.currentThread().getName());
            }
        });

        for(int i=0;i<N;i++)
            new Writer(barrier).start();
    }

    static class Writer extends Thread{
        private CyclicBarrier cyclicBarrier;
        public Writer(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            System.out.println("线程"+Thread.currentThread().getName()+"正在写入数据...");
            try {
                Thread.sleep(5000);      //以睡眠来模拟写入数据操作
                System.out.println("线程"+Thread.currentThread().getName()+"写入数据完毕，等待其他线程写入完毕");
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }catch(BrokenBarrierException e){
                e.printStackTrace();
            }
            System.out.println("所有线程写入完毕，继续处理其他任务...");
        }
    }
}
