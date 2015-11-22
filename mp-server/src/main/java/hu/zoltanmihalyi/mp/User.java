package hu.zoltanmihalyi.mp;

import hu.zoltanmihalyi.mp.event.*;
import hu.zoltanmihalyi.mp.exception.MembershipNotFoundException;

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
            Method method = event.getMethod();
            Object privilege = getEventMembership(event).getPrivilege(method.getDeclaringClass());
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
}
