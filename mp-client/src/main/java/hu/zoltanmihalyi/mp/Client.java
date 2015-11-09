package hu.zoltanmihalyi.mp;

import hu.zoltanmihalyi.mp.event.JoinEvent;
import hu.zoltanmihalyi.mp.event.LeaveEvent;
import hu.zoltanmihalyi.mp.event.ServerEvent;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Client implements Channel<ServerEvent> {
    private Map<Integer, String> roomIdToNameMap = new HashMap<>();
    private Map<EventTypeAndRoomName, Method> eventListeners = new HashMap<>();


    public Client() {
        for (Method method : this.getClass().getDeclaredMethods()) {
            Event event = getEventAnnotation(method);
            if (event != null) {
                addEventListener(event, method);
            }
        }
    }

    private Event getEventAnnotation(Method method) {
        return method.getAnnotation(Event.class);
    }

    private void addEventListener(Event event, Method method) {
        eventListeners.put(new EventTypeAndRoomName(event.type(), event.roomName()), method);
    }

    @Override
    public void onMessage(ServerEvent message) {
        if (message instanceof JoinEvent) {
            onJoin((JoinEvent) message);
        } else if (message instanceof LeaveEvent) {
            onLeave(((LeaveEvent) message));
        }
    }

    private void onJoin(JoinEvent message) {
        String roomName = message.getRoomName();
        roomIdToNameMap.put(message.getRoomId(), roomName);
        fireEvent(EventType.JOIN, roomName);
    }

    private void onLeave(LeaveEvent message) {
        String roomName = roomIdToNameMap.get(message.getRoomId());
        fireEvent(EventType.LEAVE, roomName);
    }

    private void fireEvent(EventType type, String roomName) {
        Method method = eventListeners.get(new EventTypeAndRoomName(type, roomName));
        if (method == null) {
            return;
        }
        try {
            method.invoke(this);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Illegal access on event handler!", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Event handler threw an exception!", e);
        }
    }

    @Override
    public void onClose() {

    }

    @Override
    public void onError(Exception e) {

    }

    @EqualsAndHashCode
    @AllArgsConstructor
    private static class EventTypeAndRoomName {
        private EventType eventType;
        private String roomName;
    }
}
