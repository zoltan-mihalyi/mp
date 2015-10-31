package hu.zoltanmihalyi.mp;

import java.util.function.Consumer;

public class ChannelTransformer<I, O> implements Channel<I> {

    private Channel<O> target;
    private Converter<I, O> converter;
    private Consumer<O> onMessage;
    private Consumer<ConversionFailureException> onError;

    public ChannelTransformer(Channel<O> target, Converter<I, O> converter) {
        this.target = target;
        onMessage = target::onMessage;
        onError = target::onError;
        this.converter = converter;
    }

    @Override
    public void onMessage(I message) {
        converter.convert(message, onMessage, onError);
    }

    @Override
    public void onClose() {
        target.onClose();
    }

    @Override
    public void onError(Exception e) {
        target.onError(e);
    }
}
