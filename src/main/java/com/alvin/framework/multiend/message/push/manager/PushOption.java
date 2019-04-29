package com.alvin.framework.multiend.message.push.manager;

/**
 * datetime 2019/4/29 11:17
 *
 * @author sin5
 */
public class PushOption {

    /**
     * if true, push to all valid tunnels, else only push to first connected tunnel
     */
    private PushScope pushScope;
    /**
     * if true, push repeatedly until receipt gathered, else push ignore receipt
     */
    private boolean closedLoop;
    /**
     * if true, push orderly in pushScope when closedLoop=true
     */
    private boolean ordered;

    public PushOption(PushScope pushScope, boolean closedLoop) {
        this.pushScope = pushScope;
        this.closedLoop = closedLoop;
    }

    public PushOption(PushScope pushScope, boolean closedLoop, boolean ordered) {
        if (ordered && !closedLoop) {
            throw new IllegalArgumentException("closedLoop must be true when ordered set true");
        }
        this.pushScope = pushScope;
        this.closedLoop = closedLoop;
        this.ordered = ordered;
    }

    public PushScope getPushScope() {
        return pushScope;
    }

    public boolean isClosedLoop() {
        return closedLoop;
    }

    public boolean isOrdered() {
        return ordered;
    }
}
