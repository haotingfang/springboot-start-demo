package com.example.springbootstartdemo.curatorDemo;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class AclDemo {

    private static String CONNECTION_STR="127.0.0.1:2182,127.0.0.1:2183,127.0.0.1:2184";



    public static void main(String[] args) throws Exception {
//        demo1();
        demo2();
    }

    private static void demo2() throws Exception {


        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().
                connectString(CONNECTION_STR).sessionTimeoutMs(5000).
                authorization("digest","admin:admin".getBytes()).
                retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
        curatorFramework.start();
        List<ACL> list=new ArrayList<>();
        ACL acl=new ACL(ZooDefs.Perms.READ | ZooDefs.Perms.WRITE,
                new Id("digest", DigestAuthenticationProvider.generateDigest("admin:admin")));
        list.add(acl);
        curatorFramework.setACL().withACL(ZooDefs.Ids.CREATOR_ALL_ACL).forPath("/temp");
//        curatorFramework.create().withMode(CreateMode.PERSISTENT).withACL(list).forPath("/auth");
    }

    private static void demo1() throws Exception {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder().
                connectString(CONNECTION_STR).sessionTimeoutMs(5000).
                retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
        curatorFramework.start();
        List<ACL> list=new ArrayList<>();
        ACL acl=new ACL(ZooDefs.Perms.READ | ZooDefs.Perms.WRITE,
                new Id("digest", DigestAuthenticationProvider.generateDigest("admin:admin")));
        list.add(acl);
        curatorFramework.create().withMode(CreateMode.PERSISTENT).withACL(list).forPath("/auth");

//        curatorFramework.setData().forPath("/auth","ss".getBytes());
    }
}
