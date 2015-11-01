package hu.zoltanmihalyi.mp;

public class TransformedChannel<I, O> implements Channel<I> {

    private Channel<? super O> target;
    private Converter<? super I, ? extends O> converter;
    private Callback<? super O> onMessage;
    private Callback<? super ConversionFailureException> onError;

    public TransformedChannel(Channel<? super O> target, Converter<? super I, ? extends O> converter) {
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
