package hu.zoltanmihalyi.mp.event;

public class LeaveEvent extends ServerEvent {
    public LeaveEvent(int roomId) {
        super(roomId);
    }
}
