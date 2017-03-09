package main;

import java.util.LinkedList;

/**
 * Created by BB20101997 on 05. Mär. 2017.
 */
public class GLFWQueue implements Runnable {

    private static final GLFWQueue INSTANCE = new GLFWQueue();

    private final LinkedList<Runnable> queue = new LinkedList<>();

    public static GLFWQueue getInstance() {
        return INSTANCE;
    }

    /**
     * Pushes the Runnable onto the Queue and returns immediately
     * will execute in FIFO order
     */
    public synchronized void pushEvent(final Runnable run) {
        queue.addLast(run);
        notifyAll();
    }

    /**
     * Pushes the Runnable onto the Queue and waits till it has been executed
     * <p>
     * Note:
     * If current Thread is the main Thread the Runnable will be executed immediate and not at the end of the queue
     */
    public void pushEventAndWait(final Runnable run) {
        if(isMainThread()) {
            //would never execute if we pushed and waited
            run.run();
            return;
        }
        final Object    waiter      = new Object();
        final boolean[] interrupted = {false};

        synchronized(waiter) {
            pushEvent(() -> {
                try {
                    run.run();
                }
                finally {
                    synchronized(waiter) {
                        interrupted[0] = true;
                        waiter.notifyAll();
                    }
                }
            });
            while(!interrupted[0]) {
                try {
                    waiter.wait();
                }
                catch(final InterruptedException ignore) {
                }
            }
        }

    }

    private boolean isMainThread() {
        return Thread.currentThread().getId() == 1;
    }

    private GLFWQueue() {}

    /**
     * Will run the GLFWQueue in an endless loop
     * can be used for everything that needs to be done on the main Thread
     */
    public void run() {
        //only the main Thread may handle the queue
        if(!isMainThread()) { return; }
        //noinspection InfiniteLoopStatement
        while(true) {
            while(!queue.isEmpty()) {
                try {
                    queue.pop().run();
                }
                catch(final Exception ignore) {}
            }
            synchronized(INSTANCE) {
                if(queue.isEmpty()) {
                    try {
                        INSTANCE.wait();
                    }
                    catch(final InterruptedException ignore) {
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return "GLFWQueue{" + "queue=" + queue + '}';
    }
}
