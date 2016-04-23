package hu.zoltanmihalyi.mp.event;

import lombok.Getter;

public class ReplicationEvent extends ServerEvent {
    @Getter
    private Object data;

    public ReplicationEvent(int roomId, Object data) {
        super(roomId);
        this.data = data;
    }
}
