package com.yk.zookeeper.zk;

import org.I0Itec.zkclient.exception.ZkMarshallingError;

import java.io.UnsupportedEncodingException;

/**
 * @program: work-flow
 * @description:
 * @author: yk
 * @create: 2018-08-21 11:04
 **/
public class ZkSerializer implements org.I0Itec.zkclient.serialize.ZkSerializer {

    static String CHARSET = "UTF-8";

    @Override
    public byte[] serialize(Object o) throws ZkMarshallingError {
        try {
            return o.toString().getBytes(CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new ZkMarshallingError(e);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws ZkMarshallingError {
        try {
            return new String(bytes, CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new ZkMarshallingError(e);
        }
    }
}
