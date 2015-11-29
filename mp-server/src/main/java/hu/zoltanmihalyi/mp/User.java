package hu.zoltanmihalyi.mp;

import hu.zoltanmihalyi.mp.event.*;
import hu.zoltanmihalyi.mp.exception.MembershipNotFoundException;
import hu.zoltanmihalyi.mp.exception.PrivilegeNotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class User implements Channel<ClientEvent> {
    private Channel<ServerEvent> channel;
    private BiMap<Membership, Integer> membershipIdMap = new BiMap<>();
    private int lastId = 0;

    public User(Channel<ServerEvent> channel) {
        this.channel = channel;
    }

    void join(Membership membership) {
        int id = ++lastId;
        membershipIdMap.put(membership, id);
        channel.onMessage(new JoinEvent(id, membership.getRoom().getClass().getSimpleName()));
    }

    void leave(Membership membership) {
        int id = membershipIdMap.getValue(membership);
        channel.onMessage(new LeaveEvent(id));
    }

    @Override
    public void onMessage(ClientEvent message) {
        if (message instanceof InvocationEvent) {
            InvocationEvent event = ((InvocationEvent) message);
            Privilege privilege = getEventMembership(event).getPrivilegeAsPrivilege(getEventClass(event));
            if (!privilege.isActive()) {
                throw new PrivilegeNotFoundException();
            }
            Method method = getEventMethod(privilege, event);
            try {
                method.invoke(privilege, event.getArguments());
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Privilege method invocation failed", e);
            }
        }
    }

    @Override
    public void onClose() {

    }

    @Override
    public void onError(Exception e) {

    }

    private Membership getEventMembership(ClientEvent event) {
        int roomId = event.getMembershipId();
        if (!membershipIdMap.containsValue(roomId)) {
            throw new MembershipNotFoundException("The membership of the user with the given id not found: " + roomId);
        }
        return membershipIdMap.getKey(roomId);
    }

    private static Method getEventMethod(Object privilege, InvocationEvent event) {
        try {
            String[] parameterTypeNames = event.getParameterTypes();
            Class[] parameterTypes = new Class[parameterTypeNames.length];
            for (int i = 0; i < parameterTypeNames.length; i++) {
                parameterTypes[i] = Class.forName(parameterTypeNames[i]);
            }

            return privilege.getClass().getMethod(event.getMethodName(), parameterTypes);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not find the class of parameter type described in InvocationEvent!", e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Could not find method described in InvocationEvent!", e);
        }
    }

    private static Class<?> getEventClass(InvocationEvent event) {
        try {
            return Class.forName(event.getClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not find class described in InvocationEvent!", e);
        }
    }
}
