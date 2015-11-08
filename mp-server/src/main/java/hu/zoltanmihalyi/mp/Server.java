package hu.zoltanmihalyi.mp;

import hu.zoltanmihalyi.mp.event.ServerEvent;

public abstract class Server implements ChannelAcceptor<ServerEvent, Object> {
    @Override
    public Channel<Object> accept(Channel<ServerEvent> channel) {
        onConnect(new User(channel));
        return null;
    }

    protected abstract void onConnect(User user);
}
