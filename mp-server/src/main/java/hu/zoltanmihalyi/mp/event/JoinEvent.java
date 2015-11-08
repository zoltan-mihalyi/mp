package hu.zoltanmihalyi.mp.event;

public class JoinEvent extends ServerEvent {
    public JoinEvent(int roomId) {
        super(roomId);
    }
}
