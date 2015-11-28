package hu.zoltanmihalyi.mp;

import hu.zoltanmihalyi.mp.event.ClientEvent;
import hu.zoltanmihalyi.mp.event.ServerEvent;

public abstract class Server implements ChannelAcceptor<ServerEvent, ClientEvent> {
    @Override
    public Channel<ClientEvent> accept(Channel<ServerEvent> channel) {
        User user = new User(channel);
        onConnect(user);
        return user;
    }

    protected abstract void onConnect(User user);
}
