package com.yk.zookeeper;

import com.yk.zookeeper.Service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZookeeperApplicationTests {
    private Logger log = LoggerFactory.getLogger(String.class);

    @Test
    public void contextLoads() {
    }


    @Test
    public void test(){
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
    }
}
