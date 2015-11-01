package hu.zoltanmihalyi.mp;

public abstract class SynchronousConverter<I, O> implements Converter<I, O> {

    @Override
    public final void convert(I i, Callback<? super O> callback, Callback<? super ConversionFailureException> onError) {
        O result;
        try {
            result = convert(i);
        } catch (ConversionFailureException ex) {
            onError.call(ex);
            return;
        }
        callback.call(result);
    }

    public abstract O convert(I i) throws ConversionFailureException;

}
