package practice.cxh.zhihuzhuanlan.event_pool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventPool {

    private static class InstanceHolder {
        private static EventPool sInstance = new EventPool();
    }

    public static EventPool getInstace() {
        return InstanceHolder.sInstance;
    }

    private Map<String, List<IEventListener>> evnLsnMap;
//    private Map<IEventListener, List<String>> lsnEvnMap;

    private EventPool() {
        evnLsnMap = new HashMap<>();
//        lsnEvnMap = new HashMap<>();
    }

    public void addListener(String eventType, IEventListener listener) {
        if (!evnLsnMap.containsKey(eventType)) {
            evnLsnMap.put(eventType, new ArrayList<IEventListener>());
        }
        List<IEventListener> listenerList = evnLsnMap.get(eventType);
        if (!listenerList.contains(listener)) {
            listenerList.add(listener);
        }
    }

    public void removeListener(IEventListener listener) {
        for (String eventType : evnLsnMap.keySet()) {
            removeListener(eventType, listener);
        }
    }

    public void removeListener(String eventType, IEventListener listener) {
        if (!evnLsnMap.containsKey(eventType)) {
            return;
        }
        List<IEventListener> listenerList = evnLsnMap.get(eventType);
        if (listener == null || !listenerList.contains(listener)) {
            return;
        }
        listenerList.remove(listener);
        if (listenerList.size() == 0) {
            evnLsnMap.remove(eventType);
        }
    }

    public void publishEvent(IEvent event) {
        String eventId = event.getType();
        if (!evnLsnMap.containsKey(eventId)) {
            return;
        }
        List<IEventListener> listenerList = evnLsnMap.get(eventId);
        if (listenerList == null) {
            return;
        }
        for (IEventListener listener : listenerList) {
            listener.onEvent(event);
        }
    }
}
