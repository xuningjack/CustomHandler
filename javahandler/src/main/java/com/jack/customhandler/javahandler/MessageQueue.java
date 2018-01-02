package com.jack.customhandler.javahandler;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 自定义消息队列
 * Created by Jack on 18/1/1.
 */
public class MessageQueue {


    Message[] items;
    /**
     * 消息队列的大小
     */
    private int mCount = 50;
    /**
     * 入队的索引
     */
    private int mPutIndex;
    /**
     * 出队的索引
     */
    private int mTakeIndex;

    //2个条件变量
    /**
     * 队列没有空
     */
    Condition mNotEmpty;
    /**
     * 队列没有满
     */
    Condition mNotFull;
    /**
     * 锁
     */
    Lock mLock;


    public MessageQueue() {
        this.items = new Message[mCount];   //发消息、取消息都会阻塞
        this.mLock = new ReentrantLock();
        this.mNotEmpty = mLock.newCondition();
        this.mNotFull = mLock.newCondition();
    }


    /**
     * 消息入队  在子线程中执行，生产者。
     * 需要考虑：生产过快，供大于求，会覆盖。
     *
     * @param msg
     */
    public void enqueueMessage(Message msg) {
        //生产者队列已满，停止生产，等待子线程消费
        try {
            mLock.lock();
            while (mCount == items.length) {  //队列已满，阻塞生产
                mNotFull.await();   //等待，代码不会向下执行
            }
            items[mPutIndex] = msg;
            mPutIndex = (++mPutIndex == items.length) ? 0 : mPutIndex;  //判断到头就从0开始

            //队列不再为空，有产品可以消费
            mNotEmpty.signalAll();  //全部唤醒消费者
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            mLock.unlock();
        }
        mCount++;
    }

    /**
     * 消息出队   在主线程中执行，消费者。
     * 需要考虑：消费过快，供不应求，会取空。
     *
     * @return
     */
    public Message next() {
        //消费者把队列掏空，等待生产者线程（主线程）生产
        Message msg = null;
        try {
            mLock.lock();
            while (mCount == 0) {
                mNotEmpty.await();   //等待，代码不会向下执行
            }
            msg = items[mTakeIndex];
            items[mTakeIndex] = null;  //置空，GC回收
            mTakeIndex = (++mTakeIndex == items.length) ? 0 : mTakeIndex;  //判断到头就从0开始
            mCount--;

            //消费了一个产品，队列不满，通知生产者线程生产
            mNotFull.signalAll();  //全部唤醒生产者
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mLock.unlock();
        }
        return msg;
    }

}
