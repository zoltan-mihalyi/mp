package hu.zoltanmihalyi.mp;

@FunctionalInterface
public interface Callback<T> {
    void call(T t);
}
