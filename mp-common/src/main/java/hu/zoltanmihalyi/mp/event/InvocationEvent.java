package hu.zoltanmihalyi.mp.event;

import lombok.Getter;

import java.lang.reflect.Method;

public class InvocationEvent extends ClientEvent {
    @Getter
    private final Method method;
    private final Object[] arguments;

    public InvocationEvent(int membershipId, Method method, Object[] arguments) {
        super(membershipId);
        this.method = method;
        this.arguments = arguments.clone();
    }

    public int getArgumentsNumber() {
        return arguments.length;
    }

    public Object[] getArguments() {
        return arguments.clone();
    }
}
