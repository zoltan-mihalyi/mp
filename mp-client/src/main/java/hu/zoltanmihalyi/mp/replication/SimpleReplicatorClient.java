package hu.zoltanmihalyi.mp.replication;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class SimpleReplicatorClient<T> implements ReplicatorClient<T> {
    private Class<T> dataClass;

    @Override
    public Class<T> getDataClass() {
        return dataClass;
    }
}
