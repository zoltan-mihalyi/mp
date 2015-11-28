package hu.zoltanmihalyi.mp;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TransformedChannelAcceptor<I, O, TI, TO> implements ChannelAcceptor<I, O> {
    private final ChannelAcceptor<? extends TI, ? super TO> target;
    private final Converter<? super TI, ? extends I> converter;
    private final Converter<? super O, ? extends TO> converter2;

    @Override
    public Channel<O> accept(Channel<I> channel) {
        Channel<? super TO> toChannel = target.accept(new TransformedChannel<>(channel, converter));
        return new TransformedChannel<>(toChannel, converter2);
    }
}
