package hu.zoltanmihalyi.mp;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RemoteRoom {
    private RoomChannel roomChannel;
    private InvocationHandler invocationHandler = new MyInvocationHandler();

    public RemoteRoom(RoomChannel roomChannel) {
        this.roomChannel = roomChannel;
    }

    @SuppressWarnings("unchecked")
    public <T> T getPrivilege(Class<T> privilegeInterface) {
        return (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{privilegeInterface}, invocationHandler);
    }

    private class MyInvocationHandler implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            roomChannel.sendInvocation(method,args);
            return null;
        }
    }
}