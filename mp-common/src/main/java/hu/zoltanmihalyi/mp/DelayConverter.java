package hu.zoltanmihalyi.mp;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Mihályi Zoltán
 */
public class DelayConverter<T> extends Thread implements Converter<T, T> {
    private final int timeoutInMs;
    private final BlockingQueue<DelayedCallback> queue = new LinkedBlockingQueue<>();

    private volatile boolean stopped;

    public DelayConverter(int timeoutInMs) {
        this.timeoutInMs = timeoutInMs;
    }

    @Override
    public void convert(T t, Callback<? super T> callback, Callback<? super ConversionFailureException> onError) {
        if (stopped) {
            onError.call(new ConversionFailureException("The delay thread is stopped already."));
            return;
        }
        long now = System.currentTimeMillis();
        queue.add(new DelayedCallback(now + timeoutInMs) {
            @Override
            public void call() {
                callback.call(t);
            }
        });
    }

    public synchronized void stopImmediately() {
        if (stopped) {
            return;
        }
        stopped = true;
        interrupt();
    }

    public void run() {
        while (!stopped) {
            try {
                DelayedCallback callback = queue.take();
                long remainingTime = callback.expectedExecutionTime - System.currentTimeMillis();
                if (remainingTime > 0) {
                    Thread.sleep(remainingTime);
                }
                callback.call();
            } catch (InterruptedException ignored) {
                return;
            }
        }
    }

    private abstract static class DelayedCallback {
        public final long expectedExecutionTime;

        public DelayedCallback(long expectedExecutionTime) {
            this.expectedExecutionTime = expectedExecutionTime;
        }

        public abstract void call();
    }
}
