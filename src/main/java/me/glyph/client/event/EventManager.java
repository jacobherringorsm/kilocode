package me.glyph.client.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {
    private final Map<Class<? extends Event>, List<ListenerData>> listeners = new HashMap<>();
    
    public void register(Object object) {
        for (Method method : object.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(SubscribeEvent.class)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 1 && Event.class.isAssignableFrom(parameterTypes[0])) {
                    Class<? extends Event> eventClass = (Class<? extends Event>) parameterTypes[0];
                    method.setAccessible(true);
                    listeners.computeIfAbsent(eventClass, k -> new CopyOnWriteArrayList<>())
                            .add(new ListenerData(object, method));
                }
            }
        }
    }
    
    public void unregister(Object object) {
        for (List<ListenerData> list : listeners.values()) {
            list.removeIf(data -> data.object == object);
        }
    }
    
    public <T extends Event> T post(T event) {
        List<ListenerData> list = listeners.get(event.getClass());
        if (list != null) {
            for (ListenerData data : list) {
                try {
                    data.method.invoke(data.object, event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return event;
    }
    
    private static class ListenerData {
        private final Object object;
        private final Method method;
        
        public ListenerData(Object object, Method method) {
            this.object = object;
            this.method = method;
        }
    }
}
