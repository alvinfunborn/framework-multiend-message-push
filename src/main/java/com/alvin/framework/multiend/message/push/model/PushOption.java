package com.alvin.framework.multiend.message.push.model;

/**
 * datetime 2019/4/29 11:17
 *
 * @author sin5
 */
public class PushOption {
    /**
     * if true, push repeatedly until receipt gathered, else push ignore receipt
     */
    private boolean solid;
    /**
     * if true, push orderly in pushScope when solid=true
     */
    private boolean ordered;

    public static PushOption ordered() {
        return new PushOption(true, true);
    }

    public static PushOption ofSolid(boolean solid) {
        return new PushOption(solid, false);
    }

    private PushOption(boolean solid, boolean ordered) {
        if (ordered && !solid) {
            throw new IllegalArgumentException("solid must be true when ordered set true");
        }
        this.solid = solid;
        this.ordered = ordered;
    }

    public boolean issolid() {
        return solid;
    }

    public boolean isOrdered() {
        return ordered;
    }
}
