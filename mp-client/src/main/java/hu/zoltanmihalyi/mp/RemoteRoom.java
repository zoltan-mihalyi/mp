package hu.zoltanmihalyi.mp;

import hu.zoltanmihalyi.mp.replication.ReplicatorClient;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@RequiredArgsConstructor
public class RemoteRoom {
    private final RoomChannel roomChannel;
    @Getter
    private final String name;
    private InvocationHandler invocationHandler = new MyInvocationHandler();
    @Getter
    @Setter
    private ReplicatorClient<?> replicator;

    @SuppressWarnings("unchecked")
    public <T> T getPrivilege(Class<T> privilegeInterface) {
        return (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{privilegeInterface}, invocationHandler);
    }

    private class MyInvocationHandler implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            roomChannel.sendInvocation(method, args);
            return null;
        }
    }
}