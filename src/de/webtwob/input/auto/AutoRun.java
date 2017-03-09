package de.webtwob.input.auto;

import de.webtwob.interfaces.IJARGameModel;
import de.webtwob.interfaces.IJARInput;

import java.awt.*;

/**
 * Created by BB20101997 on 17. Feb. 2017.
 */
public class AutoRun implements IJARInput {

    private boolean enabled = false;
    private IJARGameModel gameModel;
    private boolean   running;
    private final Runnable run = this::marathon;
    private Thread runner;

    public AutoRun(final IJARGameModel gameModel) {
        this.gameModel = gameModel;
    }

    private void marathon() {

        while(running) {
            if(gameModel != null && enabled) {
                final Rectangle[] rectangles = gameModel.getHurdles();
                boolean           jump       = false;
                boolean           sneak      = false;
                if(rectangles[1] != null && rectangles[1].getY() != 0) {
                    sneak = true;
                }
                inner:
                for(int i = 2; i < 7; i++) {
                    if(rectangles[i] != null) {
                        if(rectangles[i].getY() == 0) {
                            if(!sneak) {
                                jump = true;
                            }
                        } else {
                            sneak = true;
                        }
                        //noinspection UnnecessaryLabelOnBreakStatement
                        break inner;
                    }
                }

                if(sneak != gameModel.isSneaking()) {
                    gameModel.setSneaking(sneak);
                }
                if(jump) {
                    gameModel.jump();
                }
            } else {
                synchronized(run) {
                    try {
                        run.wait();
                    }
                    catch(final InterruptedException ignore) {
                    }
                }
            }
        }
    }

    @Override
    public String toString() {

        return "[Auto Run]";
    }

    @Override
    public void setEnabled(final boolean enable) {
        enabled = enable;
        if(enabled && gameModel != null) {
            synchronized(run) {
                run.notifyAll();
            }
        }
    }

    @Override
    public boolean isEnabled() {

        return enabled;
    }

    @Override
    public synchronized void start() {
        if(runner == null) {
            running = true;
            runner = new Thread(run);
            runner.setName("AutoRun");
            runner.setDaemon(true);
            runner.start();
            System.out.println("Started AutoRun Thread");
        }
    }

    @Override
    public synchronized void stop() {
        if(runner != null) {
            running = false;
            synchronized(run) {
                run.notifyAll();
            }
            while(runner.isAlive()) {
                try {
                    runner.join();
                }
                catch(final InterruptedException e) {
                    e.printStackTrace();
                }
            }
            runner = null;
            System.out.println("Stopped AutoRun Thread");
        }
    }

}
