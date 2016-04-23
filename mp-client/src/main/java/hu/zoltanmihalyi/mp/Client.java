package hu.zoltanmihalyi.mp;

import hu.zoltanmihalyi.mp.event.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client implements ChannelAcceptor<ClientEvent, ServerEvent> {
    private Map<Integer, String> roomIdToNameMap = new HashMap<>();
    private Map<EventTypeAndRoomName, List<Method>> eventListeners = new HashMap<>();
    private Channel<ClientEvent> targetChannel;

    public Client() {
        for (Method method : this.getClass().getDeclaredMethods()) {
            Event event = getEventAnnotation(method);
            if (event != null) {
                addEventListener(event, method);
            }
        }
    }

    public Channel<ServerEvent> accept(Channel<ClientEvent> targetChannel) {
        if (targetChannel == null) {
            throw new NullPointerException("Target channel cannot be null!");
        }
        if (this.targetChannel != null) {
            throw new IllegalStateException("The client already accepted a connection!");
        }
        this.targetChannel = targetChannel;
        return new ServerEventChannel();
    }

    private Event getEventAnnotation(Method method) {
        return method.getAnnotation(Event.class);
    }

    private void addEventListener(Event event, Method method) {
        EventTypeAndRoomName key = new EventTypeAndRoomName(event.type(), event.roomName());
        if (!eventListeners.containsKey(key)) {
            eventListeners.put(key, new ArrayList<>());
        }
        method.setAccessible(true);
        eventListeners.get(key).add(method);
    }

    private void onJoin(JoinEvent message) {
        String roomName = message.getRoomName();
        roomIdToNameMap.put(message.getRoomId(), roomName);
        fireEvent(EventType.JOIN, roomName, new RemoteRoom(new RoomChannelImpl(message.getRoomId())));
    }

    private void onLeave(LeaveEvent message) {
        String roomName = roomIdToNameMap.get(message.getRoomId());
        fireEvent(EventType.LEAVE, roomName, null);
    }

    private void fireEvent(EventType type, String roomName, RemoteRoom room) {
        List<Method> methods = eventListeners.get(new EventTypeAndRoomName(type, roomName));
        if (methods == null) {
            return;
        }
        for (Method method : methods) {
            invokeEventListener(method, room);
        }
    }

    private void invokeEventListener(Method method, RemoteRoom room) {
        try {
            if (method.getParameterCount() == 0) {
                method.invoke(this);
            } else {
                method.invoke(this, room);
            }

        } catch (IllegalAccessException e) {
            throw new RuntimeException("Illegal access on event handler!", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Event handler threw an exception!", e);
        }
    }

    @EqualsAndHashCode
    @AllArgsConstructor
    private static class EventTypeAndRoomName {
        private EventType eventType;
        private String roomName;
    }

    private class RoomChannelImpl implements RoomChannel {
        private int roomId;

        private RoomChannelImpl(int roomId) {
            this.roomId = roomId;
        }

        @Override
        public void sendInvocation(Method method, Object[] args) {
            if (args == null) {
                args = new Object[0];
            }
            targetChannel.onMessage(new InvocationEvent(roomId, method, args));
        }
    }

    private class ServerEventChannel implements Channel<ServerEvent> {
        @Override
        public void onMessage(ServerEvent message) {
            if (message instanceof JoinEvent) {
                onJoin((JoinEvent) message);
            } else if (message instanceof LeaveEvent) {
                onLeave(((LeaveEvent) message));
            }
        }

        @Override
        public void onClose() {

        }

        @Override
        public void onError(Exception e) {

        }

    }
}
