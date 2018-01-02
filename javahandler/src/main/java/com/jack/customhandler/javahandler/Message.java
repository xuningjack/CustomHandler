package com.jack.customhandler.javahandler;

/**
 * 自定义Message
 * Created by Jack on 18/1/1.
 */
public class Message {

    /**
     * 发送、处理消息的Handler
     */
    Handler target;
    /**
     * 消息的类别
     */
    public int what;
    /**
     * 消息自带参数
     */
    public Object obj;



    @Override
    public String toString() {
        return obj.toString();
    }
}
