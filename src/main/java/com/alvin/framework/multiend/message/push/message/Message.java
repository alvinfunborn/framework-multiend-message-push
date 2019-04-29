package com.alvin.framework.multiend.message.push.message;

import com.alvin.framework.multiend.message.push.manager.PushOption;

/**
 * datetime 2019/4/29 11:24
 *
 * @author sin5
 */
public class Message {

    private String messageId;
    private String receiver;
    private String data;
    private PushOption pushOption;

    public Message(String messageId, String receiver, String data, PushOption pushOption) {
        this.messageId = messageId;
        this.receiver = receiver;
        this.data = data;
        this.pushOption = pushOption;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public PushOption getPushOption() {
        return pushOption;
    }

    public void setPushOption(PushOption pushOption) {
        this.pushOption = pushOption;
    }
}
