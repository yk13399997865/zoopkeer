package com.yk.zookeeper.Service;

import com.yk.zookeeper.zk.DistributedLock;
import com.yk.zookeeper.zk.GenerateOrderNo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;

/**
 * @program: zookpeeper
 * @description: 订单服务
 *
 * @author: yk
 **/
public class OrderService {

    private Logger log = LoggerFactory.getLogger(OrderService.class);

    //模拟生成订单的服务
    private static GenerateOrderNo g = new GenerateOrderNo();
    //锁
    private static Lock lock = new DistributedLock();

    public void createOrder() {
        String orderNo;
        //获取锁
        try {
            lock.lock();
            //获取单号
            orderNo = g.getOrderCode();
        } finally {
            lock.unlock();
        }
        log.info(Thread.currentThread().getName() + "----订单号为:" + orderNo);

    }


}
