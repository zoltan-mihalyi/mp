package hu.zoltanmihalyi.mp.event;

import lombok.Getter;

import java.lang.reflect.Method;

public class InvocationEvent extends ClientEvent {
    @Getter
    private final Method method;
    private final Object[] args;

    public InvocationEvent(int roomId, Method method, Object[] args) {
        super(roomId);
        this.method = method;
        this.args = args.clone();
    }

    public int getArgumentsNumber() {
        return args.length;
    }
}
