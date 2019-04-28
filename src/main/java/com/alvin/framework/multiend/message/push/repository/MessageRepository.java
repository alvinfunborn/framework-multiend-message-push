package com.alvin.framework.multiend.message.push.repository;

/**
 * datetime 2019/4/28 16:58
 *
 * @author sin5
 */
public interface MessageRepository<T> {

    /**
     * add to queue
     * need to be concurrent with receiver
     *
     * @param receiver receiver
     * @param obj msg obj
     * @param head if add to head
     */
    void add(String receiver, T obj, boolean head);

    /**
     * take from queue. non block
     * need to be concurrent with receiver
     *
     * @param receiver receiver
     * @return obj
     */
    T take(String receiver);

    /**
     * parse obj to str
     *
     * @param receiver receiver
     * @param obj msg obj
     * @return String
     */
    String parse(String receiver, T obj);

    /**
     * get message id from msg obj
     * @param msg msg obj
     * @return message id
     */
    String getMessageId(T msg);
}
