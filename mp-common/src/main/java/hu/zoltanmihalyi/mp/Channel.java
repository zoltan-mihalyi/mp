package hu.zoltanmihalyi.mp;

public interface Channel<T> {
    void onMessage(T message);

    void onClose();

    void onError(Exception e);
}
