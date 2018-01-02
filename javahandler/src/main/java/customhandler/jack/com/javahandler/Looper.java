package customhandler.jack.com.javahandler;



/**
 * 自定义Looper
 * Created by Jack on 18/1/1.
 */
public class Looper {

    static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal<Looper>();
    MessageQueue mQueue;


    public Looper(){
        mQueue = new MessageQueue();
    }


    /**
     * 返回当前线程绑定的Looper对象
     * @return
     */
    public static Looper myLooper(){
        return sThreadLocal.get();
    }


    /**
     * 准备，创建Looper对象
     */
    public static void prepare(){
        if(sThreadLocal.get() != null){   //主线程只有一个looper对象
            throw new RuntimeException("Only one Looper may be " +
                    "created per thread");
        }
        //当前线程绑定一个Looper对象
        sThreadLocal.set(new Looper());
    }


    /**
     * 轮询器，调用消息
     * 调用loop前一定要执行prepare
     */
    public static void loop(){
        Looper looper = myLooper();
        if(looper == null){
            throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
        }
        //当前线程的消息队列
        MessageQueue messageQueue = looper.mQueue;
        for(;;){
            Message msg = messageQueue.next();
            if(msg == null){
                continue;
            }
            //转发
            msg.target.dispatchMessage(msg);
        }
    }


}
