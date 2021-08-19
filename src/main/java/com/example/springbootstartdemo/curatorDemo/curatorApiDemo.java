/*
package com.example.springbootstartdemo.curatorDemo;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.util.List;

public class curatorApiDemo {

    public static void main(String[] args) throws Exception {
        // 多个地址逗号隔开
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("localhost:2182,localhost:2183,localhost:2184")
                .sessionTimeoutMs(1000)    // 连接超时时间
                .connectionTimeoutMs(1000) // 会话超时时间
                // 刚开始重试间隔为1秒，之后重试间隔逐渐增加，最多重试不超过三次
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();

        String dataStr = "search";
        byte[] data = dataStr.getBytes();

        // 创建节点
        // client.create().forPath("/search/business/test", data);
//        client.create().forPath("/search", data);
//        client.create().forPath("/search/business", "business".getBytes());
//        client.create().forPath("/search/business1", "business1".getBytes());

//        // 判断是否存在，Stat就是对znode所有属性的一个映射，stat=null表示节点不存在
//        Stat stat = client.checkExists().forPath("/search/business");
//        System.out.println("stat........");
//        System.out.println(stat);
//
//        // 查询子节点
//        List<String> childNodes = client.getChildren()
//                .forPath("/search");
//        System.out.println("childNode..........");
//        for(String childNode:childNodes){
//            System.out.println(childNode);
//        }

//        // 创建节点
//        client.create().creatingParentsIfNeeded() // 若创建节点的父节点不存在则先创建父节点再创建子节点
//                .withMode(CreateMode.PERSISTENT) // 创建的是持久节点
//                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE) // 默认匿名权限,权限scheme id:'world,'anyone,:cdrwa
//                .forPath("/search/business2/test","1".getBytes());
//        System.out.println("创建节点：/search/business2/test 1");

//        // 读取节点数据
//        Stat stat1 = new Stat(); // Stat就是对znode所有属性的一个映射，stat=null表示节点不存在
//        String re1 = new String(client.getData()
//                .storingStatIn(stat1) // 在获取节点内容的同时把状态信息存入Stat对象，如果不写的话只会读取节点数据
//                .forPath("/search/business2/test"));
//        System.out.println("re1......."+re1);

//        // 更新节点数据
//        client.setData()
//                .withVersion(2) // 乐观锁
//                .forPath("/search/business","2".getBytes());
//        System.out.println("更新节点：/search/business 2");



//        // 更新节点数据
//        client.setData()
////                .withVersion(3) // 乐观锁 get -s /search/business2/test  与dataVersion一致
//                .forPath("/search/business2/test","0".getBytes());
//        System.out.println("更新节点：/search/business2/test 0");

//
//        // 读取节点数据
//        Stat stat2 = new Stat(); // Stat就是对znode所有属性的一个映射，stat=null表示节点不存在
//        String re2 = new String(client.getData()
//                .storingStatIn(stat1) // 在获取节点内容的同时把状态信息存入Stat对象，如果不写的话只会读取节点数据
//                .forPath("/search/business2/test"));
//        System.out.println("re2......."+re2);


//        // 删除节点
//        client.delete()
//                .guaranteed() // 保障机制，若未删除成功，只要会话有效会在后台一直尝试删除
//                .deletingChildrenIfNeeded() // 若当前节点包含子节点，子节点也删除
//                .withVersion(2) // 指定版本号
//                .forPath("/search/business2/test");


//        // watcher事件,使用usingWatcher的时候,监听只会触发一次，监听完毕后就销毁
//        client.getData()
//                .usingWatcher(new CuratorWatcher() {
//                    @Override
//                    public void process(WatchedEvent event) throws Exception {
//                        //event.getPath()为啥是null
//                        System.out.println("触发watcher, path："+event.getPath());
//                    }
//                })
//                .forPath("/search/business2/test");
        

//        // watcher事件, NodeCache一次注册N次监, 缺点:没法监听当前节点增删改操作,所以引出了PathChildrenCache
//        final NodeCache nodeCache = new NodeCache(client, "/search/business2/test");
//        nodeCache.start(true); // 当zkServer与客户端链接的时候, NodeCache会把zk数据缓存到我们本地
//        if (nodeCache.getCurrentData() != null) {
//           System.out.println("节点初始化数据为:"+ new String(nodeCache.getCurrentData().getData()));
//        } else {
//           System.out.println("节点初始化数据为空");
//        }
//        nodeCache.getListenable().addListener(new NodeCacheListener() {
//            @Override
//            public void nodeChanged() throws Exception {
//                if (nodeCache.getCurrentData() != null) {
//                    String re = new String(nodeCache.getCurrentData().getData());
//                   System.out.println("节点路径: "+ nodeCache.getCurrentData().getPath() + "节点数据: "+re);
//                } else {
//                   System.out.println("当前节点被删除了");
//                }
//            }
//        });



//        // 监听父节点以下所有的子节点, 当子节点发生变化的时候(增删改)都会监听到
//// 为子节点添加watcher事件
//// PathChildrenCache监听数据节点的增删改
        final PathChildrenCache childrenCache = new PathChildrenCache(client, "/search", true);
// NORMAL:异步初始化, BUILD_INITIAL_CACHE:同步初始化, POST_INITIALIZED_EVENT:异步初始化,初始化之后会触发事件
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        List<ChildData> childDataList = childrenCache.getCurrentData(); // 当前数据节点的子节点数据列表
        childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                if (event.getType().equals(PathChildrenCacheEvent.Type.INITIALIZED)) {
                   System.out.println("子节点初始化ok..");
                } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)) {
                   System.out.println("添加子节点, path: "+event.getData().getPath()+", data: "+ event.getData().getData());
                } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                   System.out.println("删除子节点, path: "+event.getData().getPath());
                } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)) {
                   System.out.println("修改子节点, path: " + event.getData().getPath()+"，, data: "+event.getData().getData());
                }
            }
        });























        //...
        int n = System.in.read();
        if(n==1){
            CloseableUtils.closeQuietly(client);//建议放在finally块中
        }


    }
}
*/
