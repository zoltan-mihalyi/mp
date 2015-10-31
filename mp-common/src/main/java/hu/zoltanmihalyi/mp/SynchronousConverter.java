package hu.zoltanmihalyi.mp;

import java.util.function.Consumer;

public abstract class SynchronousConverter<I, O> implements Converter<I, O> {

    @Override
    public final void convert(I i, Consumer<? super O> callback, Consumer<? super ConversionFailureException> onError) {
        O result;
        try {
            result = convert(i);
        } catch (ConversionFailureException ex) {
            onError.accept(ex);
            return;
        }
        callback.accept(result);
    }

    public abstract O convert(I i) throws ConversionFailureException;

}
