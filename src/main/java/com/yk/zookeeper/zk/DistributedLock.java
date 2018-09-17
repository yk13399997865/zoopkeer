package com.yk.zookeeper.zk;

import com.yk.zookeeper.controller.ApiController;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @program: zookeeper
 * @description: zookeeper分布式锁
 * @author: yk

 **/
public class DistributedLock implements Lock {
    private Logger log = LoggerFactory.getLogger(DistributedLock.class);

    // 根节点
    private static String LOCK_PATH = "/QWE";

    private ThreadLocal<String> currentPath = new ThreadLocal<>();

    private ThreadLocal<String> beforePath = new ThreadLocal<>();

    private ZkClient zk = null; //客户端


    private void init() {
        if (zk == null) {
            zk = new ZkClient("localhost:2181");
            zk.setZkSerializer(new ZkSerializer());
        }
    }

    public DistributedLock() {
        super();
        try {
            this.zk.createPersistent(LOCK_PATH);
        } catch (Exception e) {
            System.out.println("当前节点已存在");
        }
        init();
    }

    private void waitForLock() {
        //阻塞自己计数器
        CountDownLatch cd = new CountDownLatch(1);
        //注册一个watcher
        IZkDataListener listener = new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o) {
            }

            @Override
            public void handleDataDeleted(String s) {
                //收到节点删除的通知
                cd.countDown();//
                log.info("收到删除通知");
            }
        };
        //订阅
        this.zk.subscribeDataChanges(this.beforePath.get(), listener);
        //阻塞
        if (zk.exists(this.beforePath.get())) {
            try {
                cd.await();  //让当前线程阻塞，通过监听watcher收到删除通知则唤醒该线程
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //移除订阅
        this.zk.unsubscribeDataChanges(this.beforePath.get(), listener);
    }


    @Override
    public void lock() {
        if (!tryLock()) {
            //创建锁不成功，阻塞自己
            waitForLock();
            lock();
        }
    }

    @Override
    public void lockInterruptibly() {

    }

    @Override
    public boolean tryLock() {
        if (currentPath.get() == null) {
            //创建临时有序节点
            currentPath.set(this.zk.createEphemeralSequential(LOCK_PATH + "/", "1"));
        }
        //获取当前所有子节点
        List<String> children = this.zk.getChildren(LOCK_PATH);
        //排序
        Collections.sort(children);
        if (currentPath.get().equals(LOCK_PATH + "/" + children.get(0))) {
            return true;
        }
        //当前节点索引号
        int index = Collections.binarySearch(children,
                this.currentPath.get().substring(LOCK_PATH.length() + 1));
        beforePath.set(LOCK_PATH + "/" + children.get(index - 1));
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) {
        return false;
    }

    @Override
    public void unlock() {
        //删除节点释放锁
        this.zk.delete(this.currentPath.get());
    }

    @Override
    public Condition newCondition() {
        return null;
    }


}
