package hu.zoltanmihalyi.mp;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.function.Consumer;

import org.junit.Test;

public class SynchronousConverterTest {
    Consumer<Object> EMPTY_CALLBACK = o -> {
    };

    @Test
    public void testCallbackCalledWithConvertedParameter() {
        SynchronousConverter<Object, String> toStringConverter = new ToStringConverter();
        @SuppressWarnings("unchecked")
        Consumer<String> callback = mock(Consumer.class);
        toStringConverter.convert(1, callback, EMPTY_CALLBACK);
        verify(callback).accept("1");
    }

    @Test
    public void testErrorCallbackCalledWhenExceptionThrown() {
        SynchronousConverter<Object, Object> failingConverter = new FailingConverter();
        @SuppressWarnings("unchecked")
        Consumer<ConversionFailureException> errorCallback = mock(Consumer.class);

        failingConverter.convert(new Object(), EMPTY_CALLBACK, errorCallback);
        verify(errorCallback).accept(any(ConversionFailureException.class));
    }

    private static class ToStringConverter extends SynchronousConverter<Object, String> {
        @Override
        public String convert(Object o) throws ConversionFailureException {
            return o.toString();
        }
    }

    private static class FailingConverter extends SynchronousConverter<Object, Object> {
        @Override
        public Object convert(Object o) throws ConversionFailureException {
            throw new ConversionFailureException("test");
        }
    }
}
