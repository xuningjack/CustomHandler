Custom Handler mechanism to achieve communication between threads.（自定义Handler机制，实现线程间的通信）

Handler中有Looper mLooper和MessageQueue mMessageQueue。
Message中有Handler target。
Looper中有MessageQueue mQueue。里面是死循环，一直从MessageQueue中获取Message对象，交给Handler处理。
MessageQueue中有存储消息的Message[] items。
