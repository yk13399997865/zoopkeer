package com.yk.zookeeper.zk;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @program: zookeeper
 * @description: 模拟生成订单号
 * @author: yk
 **/
public class GenerateOrderNo {

    private static Long UNIQUE = 0L;

    private static Integer LENGTH = 5;

    private static int I = 0;

    public static void main(String[] args) {
        GenerateOrderNo o = new GenerateOrderNo();
        for (int i = 0; i < 20; i++) {

            System.out.println(o.getOrderNo());
        }

    }

    public String getOrderCode() {
        String time = getTime("yyyy-MM-dd-HH-mm-ss-");
        return time + ++I;

    }

    public String getOrderNo() {
        if (UNIQUE == 0) {
            UNIQUE = 1L;
        } else {
            UNIQUE += 1;
        }
        String prefix = getTime("yyyy-MM-dd-HH-");
        String orderNum = prefix + addZero(UNIQUE.toString(), LENGTH);
        if (UNIQUE >= getMax(LENGTH)) {
            UNIQUE = 0L;
        }
        return orderNum;
    }

    public String getTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    /**
     * 获取指定长度最大的值
     *
     * @param len 长度
     * @return LONG
     */
    public Long getMax(Integer len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append("9");
        }
        return Long.parseLong(sb.toString());
    }

    /**
     * 不足则补齐指定长度的0
     *
     * @param str    字符串
     * @param length 指定长度
     * @return string
     */
    private String addZero(String str, int length) {
        int strLen = str.length();
        StringBuilder sb;
        while (strLen < length) {
            sb = new StringBuilder();
            sb.append("0").append(str);
            str = sb.toString();
            strLen = str.length();
        }
        return str;
    }
}
