package practice.cxh.zhihuzhuanlan.event_pool.event;

import practice.cxh.zhihuzhuanlan.event_pool.IEvent;

public class SubscribeEvent implements IEvent {
    public static final String TYPE = "subscribe_event";

    private String columnSlug;
    private boolean subscribe;

    public SubscribeEvent(String columnSlug, boolean subscribe) {
        this.columnSlug = columnSlug;
        this.subscribe = subscribe;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public String getColumnSlug() {
        return columnSlug;
    }

    public void setColumnSlug(String columnSlug) {
        this.columnSlug = columnSlug;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }
}
