package hu.zoltanmihalyi.mp;

import hu.zoltanmihalyi.mp.event.JoinEvent;
import hu.zoltanmihalyi.mp.event.LeaveEvent;
import hu.zoltanmihalyi.mp.event.ServerEvent;

import java.util.HashMap;
import java.util.Map;

public class User {
    private Channel<ServerEvent> channel;
    private Map<Room, Integer> roomIdMap = new HashMap<>();
    private int lastId = 0;

    public User(Channel<ServerEvent> channel) {
        this.channel = channel;
    }

    void join(Room room) {
        int id = ++lastId;
        roomIdMap.put(room, id);
        channel.onMessage(new JoinEvent(id, room.getClass().getSimpleName()));
    }

    void leave(Room room) {
        int id = roomIdMap.get(room);
        channel.onMessage(new LeaveEvent(id));
    }
}
