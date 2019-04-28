package com.alvin.framework.multiend.message.push.locker;

/**
 * datetime 2019/4/10 17:36
 *
 * @author sin5
 */
public interface MessagePushLocker {

    /**
     * lock receiver process
     *
     * @param receiver reciever
     * @return true if lock succedd
     */
    boolean tryLock(String receiver);

    /**
     * unlock receiver process
     *
     * @param receiver receiver
     */
    void unlock(String receiver);
}
