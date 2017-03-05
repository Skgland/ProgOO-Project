package main;

import java.util.LinkedList;

/**
 * Created by BB20101997 on 05. Mär. 2017.
 */
public class GLFWQueue implements Runnable {

    private static final GLFWQueue instance = new GLFWQueue();

    private LinkedList<Runnable> queue = new LinkedList<>();

    public static GLFWQueue getInstance() {
        return instance;
    }

    public synchronized void pushEvent(Runnable run) {
        queue.addLast(run);
        notifyAll();
    }

    public void pushEventAndWait(Runnable run) {
        if(!isRunning){
            pushEvent(run);
            return;
        }
        final Object    waiter     = new Object();
        final boolean[] interrupted = {false};
        pushEvent(() -> {
            run.run();
            synchronized(waiter) {
                interrupted[0] = true;
                waiter.notifyAll();
            }
        });
        synchronized(waiter) {
            if(!interrupted[0]) {
                try {
                    waiter.wait();
                }
                catch(InterruptedException ignore) {
                }
            }
        }

    }

    private GLFWQueue() {}

    public void stop(){
        run = false;
    }

    private boolean isRunning = false;
    private boolean run = true;

    public void run() {
        isRunning = true;
        while(run) {
            while(!queue.isEmpty()) {
                try {
                    queue.pop().run();
                }catch(Exception ignore){}
            }
            synchronized(instance) {
                if(queue.isEmpty()) {
                    try {
                        instance.wait();
                    }
                    catch(InterruptedException ignore) {
                    }
                }
            }
        }
        isRunning = false;
    }

}
