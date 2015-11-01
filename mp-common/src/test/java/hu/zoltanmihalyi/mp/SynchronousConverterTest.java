package hu.zoltanmihalyi.mp;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class SynchronousConverterTest {
    Callback<Object> EMPTY_CALLBACK = o -> {
    };

    @Test
    public void testCallbackCalledWithConvertedParameter() {
        SynchronousConverter<Object, String> toStringConverter = new ToStringConverter();
        @SuppressWarnings("unchecked")
        Callback<String> callback = mock(Callback.class);
        toStringConverter.convert(1, callback, EMPTY_CALLBACK);
        verify(callback).call("1");
    }

    @Test
    public void testErrorCallbackCalledWhenExceptionThrown() {
        SynchronousConverter<Object, Object> failingConverter = new FailingConverter();
        @SuppressWarnings("unchecked")
        Callback<ConversionFailureException> errorCallback = mock(Callback.class);

        failingConverter.convert(new Object(), EMPTY_CALLBACK, errorCallback);
        verify(errorCallback).call(any(ConversionFailureException.class));
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
