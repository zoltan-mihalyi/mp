package hu.zoltanmihalyi.mp.event;

import lombok.Getter;

@Getter
public class JoinEvent extends ServerEvent {
    private final String roomName;

    public JoinEvent(int roomId, String roomName) {
        super(roomId);
        this.roomName = roomName;
    }
}
