package hu.zoltanmihalyi.mp;

public class SerializerChannelAcceptor<I, O> extends TransformedChannelAcceptor<byte[], byte[], I, O> {
    public SerializerChannelAcceptor(ChannelAcceptor<? extends I, ? super O> target, Class<? extends O> oClass) {
        super(target, new Serializer(), new Deserializer<>(oClass));
    }
}
