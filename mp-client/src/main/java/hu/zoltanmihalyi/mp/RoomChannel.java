package hu.zoltanmihalyi.mp;

import java.lang.reflect.Method;

public interface RoomChannel {
    void sendInvocation(Method method, Object[] args);
}
