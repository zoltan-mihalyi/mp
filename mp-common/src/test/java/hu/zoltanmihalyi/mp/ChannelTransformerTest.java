package hu.zoltanmihalyi.mp;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class ChannelTransformerTest {

    @Test
    public void testMessage() {
        @SuppressWarnings("unchecked")
        Channel<String> channel = mock(Channel.class);
        ChannelTransformer<Object, String> toStringTransformer = new ChannelTransformer<>(channel,
                (o, callback, onError) -> callback.accept(o.toString()));

        toStringTransformer.onMessage(12);
        verify(channel).onMessage("12");
    }

    @Test
    public void testOnErrorForwarded() {
        @SuppressWarnings("unchecked")
        Channel<Object> channel = mock(Channel.class);
        ChannelTransformer<Object, Object> identityTransformer = new IdentityTransformer<>(channel);

        Exception exception = mock(Exception.class);
        identityTransformer.onError(exception);
        verify(channel).onError(exception);
    }

    @Test
    public void testOnCloseForwarded() {
        @SuppressWarnings("unchecked")
        Channel<Object> channel = mock(Channel.class);
        ChannelTransformer<Object, Object> identityTransformer = new IdentityTransformer<>(channel);

        identityTransformer.onClose();
        verify(channel).onClose();
    }

    private static class IdentityTransformer<T> extends ChannelTransformer<T, T> {
        public IdentityTransformer(Channel<T> target) {
            super(target, (o, callback, onError) -> callback.accept(o));
        }
    }
}
