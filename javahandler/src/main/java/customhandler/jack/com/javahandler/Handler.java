package customhandler.jack.com.javahandler;

/**
 * 自定义Handler
 * Created by Jack on 18/1/1.
 */
public class Handler {

    private Looper mLooper;
    private MessageQueue mMessageQueue;


    public Handler(){
        mLooper = Looper.myLooper();
        mMessageQueue = mLooper.mQueue;
    }

    /**
     * 发送消息
     * @param msg
     */
    public void sendMessage(Message msg){
        msg.target = this;   //消息发送出去后，本Handler处理
        mMessageQueue.enqueueMessage(msg);
    }

    /**
     * 转发消息
     * @param msg
     */
    public void dispatchMessage(Message msg){
        handleMessage(msg);
    }

    /**
     * 处理消息，需要调用方重写
     * @param msg
     */
    public void handleMessage(Message msg){

    }

}
