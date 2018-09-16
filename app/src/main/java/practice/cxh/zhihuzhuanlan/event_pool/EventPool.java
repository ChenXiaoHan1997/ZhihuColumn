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

    public void addListener(String eventId, IEventListener listener) {
        if (!evnLsnMap.containsKey(eventId)) {
            evnLsnMap.put(eventId, new ArrayList<IEventListener>());
        }
        List<IEventListener> listenerList = evnLsnMap.get(eventId);
        if (!listenerList.contains(listener)) {
            listenerList.add(listener);
        }
    }

    public void removeListener(String eventId, IEventListener listener) {
        if (!evnLsnMap.containsKey(eventId)) {
            return;
        }
        List<IEventListener> listenerList = evnLsnMap.get(eventId);
        if (listener == null || !listenerList.contains(listener)) {
            return;
        }
        listenerList.remove(listener);
        if (listenerList.size() == 0) {
            evnLsnMap.remove(eventId);
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
