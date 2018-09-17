package com.yk.zookeeper.controller;


import com.yk.zookeeper.Service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

@RestController
public class ApiController {

    private Logger log = LoggerFactory.getLogger(ApiController.class);

    @RequestMapping({""})
    public String index(){
        int num = 30;

        //循环屏障：一个同步辅助类，它允许一组线程互相等待，直到到达某个公共屏障点
        CyclicBarrier cb = new CyclicBarrier(num);

        for (int i = 0; i < num; i++) {
            new Thread(() -> {
                OrderService os = new OrderService();
                log.info("准备中");
                try {
                    cb.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
                log.info("开始执行");
                //调用创建订单号服务
                os.createOrder();
            }).start();

        }
        log.info("我先走了");
        return "ok";
    }

}
