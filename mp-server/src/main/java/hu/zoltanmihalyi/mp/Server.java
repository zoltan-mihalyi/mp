package hu.zoltanmihalyi.mp;

public abstract class Server implements ChannelAcceptor {
    @Override
    public Channel accept(Channel channel) {
        onConnect(null);
        return null;
    }

    protected abstract void onConnect(User user);
}
