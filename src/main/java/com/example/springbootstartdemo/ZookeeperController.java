package com.example.springbootstartdemo;

import org.example.ZooKeeperTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class ZookeeperController {

    @Autowired
    ZooKeeperTemplate zooKeeperTemplate;


    @GetMapping("/create")
    public String create(@RequestParam("path") String path , @RequestParam("node") String node ){
        String res = zooKeeperTemplate.createNode(path, node);
        return res;
    }

    @GetMapping("/create2")
    public String create2(@RequestParam("path") String path , @RequestParam("node") String node , @RequestParam("value") String value){
        String res = zooKeeperTemplate.createNode(path, node, value);
        return res;
    }


    @GetMapping("/get")
    public String get(@RequestParam("path") String path , @RequestParam("node") String node){
        String res = zooKeeperTemplate.get(path, node);
        return res;
    }

    @GetMapping("/getChildren")
    public List<String> getChildren(@RequestParam("path") String path ){
        List<String> childrenList = zooKeeperTemplate.getChildren(path);
        return childrenList;
    }

//    http://localhost:8082/exists?path=root&node=htf3
    @GetMapping("/exists")
    public Boolean exists(@RequestParam("path") String path , @RequestParam("node") String node){
        Boolean flag = zooKeeperTemplate.exists(path, node);
        return flag;
    }

//    http://localhost:8082/update?path=root&node=hh&value=2
    @GetMapping("/update")
    public String update(@RequestParam("path") String path , @RequestParam("node") String node , @RequestParam("value") String value){
        String res = zooKeeperTemplate.update(path, node, value);
        return res;
    }

//http://localhost:8082/delete?path=root&node=hh
    @GetMapping("/delete")
    public Boolean delete(@RequestParam("path") String path , @RequestParam("node") String node ){
        Boolean flag = zooKeeperTemplate.delete(path, node);
        return flag;
    }


//    http://localhost:8082/lock?path=/lock1
    //锁
    @GetMapping("/lock")
    public String lock(@RequestParam("path") String path , @RequestParam("time") long time){
        MyThread myThread1 = new MyThread("线程1");
        MyThread myThread2 = new MyThread("线程2");
        zooKeeperTemplate.lock(path, time, TimeUnit.SECONDS , myThread1);
        zooKeeperTemplate.lock(path, time, TimeUnit.SECONDS , myThread2);
        return "success";
    }





}
