package hu.zoltanmihalyi.mp.replication;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public class BruteForceReplicator<T> implements Replicator<T> {
    @NonNull
    private final T target;
    private final Function<T, T> cloner;
    private T last;

    @Override
    public T getData() {
        if (last == null || !last.equals(target)) {
            last = cloner.apply(target);
            return last;
        }
        return null;
    }
}
