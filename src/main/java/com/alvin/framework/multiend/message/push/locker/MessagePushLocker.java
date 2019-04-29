package com.alvin.framework.multiend.message.push.locker;

/**
 * datetime 2019/4/10 17:36
 *
 * @author sin5
 */
public interface MessagePushLocker {

    /**
     * lock push process
     *
     * @param lockKey lockKey
     * @return true if lock succedd
     */
    boolean tryLock(String lockKey);

    /**
     * unlock push process
     *
     * @param lockKey lockKey
     */
    void unlock(String lockKey);
}
