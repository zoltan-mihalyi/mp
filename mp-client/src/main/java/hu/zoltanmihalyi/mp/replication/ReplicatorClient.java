package hu.zoltanmihalyi.mp.replication;

public interface ReplicatorClient<T> {
    void putData(T data);
    Class<T> getDataClass();
}
