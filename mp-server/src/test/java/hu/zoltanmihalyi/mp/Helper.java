package hu.zoltanmihalyi.mp;

import static org.junit.Assert.fail;

public final class Helper {
    public static void verifyException(Class<? extends Exception> exceptionClass, Action action) {
        try {
            action.execute();
        } catch (Exception e) {
            if (e.getClass() == exceptionClass) {
                return;
            }
            fail("Thrown exception was " + e.getClass() + " instead of " + exceptionClass);
        }
        fail("No exception was thrown! Expected: " + exceptionClass);
    }

    @FunctionalInterface
    public interface Action {
        void execute() throws Exception;
    }
}
