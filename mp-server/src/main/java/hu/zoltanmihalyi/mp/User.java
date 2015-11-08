package hu.zoltanmihalyi.mp;

import hu.zoltanmihalyi.mp.event.ServerEvent;
import lombok.experimental.Delegate;

public class User implements Channel<ServerEvent> {
    @Delegate
    private Channel<ServerEvent> channel;

    public User(Channel<ServerEvent> channel) {
        this.channel = channel;
    }
}
