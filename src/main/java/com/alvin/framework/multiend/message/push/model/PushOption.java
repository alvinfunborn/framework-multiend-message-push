package com.alvin.framework.multiend.message.push.model;

/**
 * datetime 2019/4/29 11:17
 *
 * @author sin5
 */
public class PushOption {

    /**
     * try once, ignore tunnel connection and receipt.
     */
    public static PushOption ONE_TIME_ATTEMPT = new PushOption(true, false, false);
    /**
     * push when tunnel connected, ignore receipt.
     */
    public static PushOption UNRELIABLE_PUSH = new PushOption(false, false, false);
    /**
     * push when tunnel connected, make sure receipt reported.
     */
    public static PushOption RELIABLE_PUSH = new PushOption(false, false, false);
    /**
     * push when tunnel connected, push next message after last receipt reported.
     */
    public static PushOption ORDERED_PUSH = new PushOption(false, true, true);

    /**
     * try one time
     */
    private boolean oneTimeAttempt;
    /**
     * if true, push repeatedly until receipt gathered, else push ignore receipt
     */
    private boolean reliable;
    /**
     * if true, push orderly in pushScope when reliable=true
     */
    private boolean ordered;

    private PushOption(boolean oneTimeAttempt, boolean reliable, boolean ordered) {
        this.oneTimeAttempt = oneTimeAttempt;
        this.reliable = reliable;
        this.ordered = ordered;
    }

    public boolean isOneTimeAttempt() {
        return oneTimeAttempt;
    }

    public boolean isReliable() {
        return reliable;
    }

    public boolean isOrdered() {
        return ordered;
    }
}
