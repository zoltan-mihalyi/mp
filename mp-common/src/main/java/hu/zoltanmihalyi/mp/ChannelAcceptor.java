package hu.zoltanmihalyi.mp;

public interface ChannelAcceptor<I, O> {
    Channel<O> accept(Channel<I> channel);
}
