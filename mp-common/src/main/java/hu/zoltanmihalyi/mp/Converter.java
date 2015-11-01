package hu.zoltanmihalyi.mp;

public interface Converter<I, O> {
    void convert(I i, Callback<? super O> callback, Callback<? super ConversionFailureException> onError);
}
