package com.example.springbootstartdemo;

public class MyThread implements Runnable {

    private String name ;

    public MyThread(String name){
        this.name = name ;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(5000);
            for(int i = 0 ; i <10000 ; i++ ){
                System.out.println(name+"--------MyThread-----"+i);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
