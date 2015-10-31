package hu.zoltanmihalyi.mp;

import java.util.function.Consumer;

public interface Converter<I, O> {
    void convert(I i, Consumer<O> callback, Consumer<ConversionFailureException> onError);
}
