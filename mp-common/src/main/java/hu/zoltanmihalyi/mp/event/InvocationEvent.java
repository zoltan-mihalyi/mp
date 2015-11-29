package hu.zoltanmihalyi.mp.event;

import lombok.Getter;

import java.lang.reflect.Method;

public class InvocationEvent extends ClientEvent {
    @Getter
    private final String className;
    @Getter
    private final String methodName;

    private final String[] parameterTypes;
    private final Object[] arguments;

    public InvocationEvent(int membershipId, Method method, Object[] arguments) {
        super(membershipId);
        className = method.getDeclaringClass().getName();
        methodName = method.getName();

        Class<?>[] parameterTypes = method.getParameterTypes();
        this.parameterTypes = new String[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            this.parameterTypes[i] = parameterTypes[i].getName();
        }

        this.arguments = arguments.clone();
    }

    public int getArgumentsNumber() {
        return arguments.length;
    }

    public String[] getParameterTypes() {
        return parameterTypes.clone();
    }

    public Object[] getArguments() {
        return arguments.clone();
    }
}
