package de.webtwob.input.gameloop;

import de.webtwob.interfaces.IJARInput;
import de.webtwob.interfaces.IJARModel;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by BB20101997 on 08. Mär. 2017.
 */
public class GameLoop implements IJARInput {

    private IJARModel model;
    private Timer timer;
    private boolean enabled = true;

    @Override
    public void setEnabled(boolean enable) {
            enabled = enable;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void linkModel(IJARModel ijarm) {
        model = ijarm;
    }

    @Override
    public synchronized void start() {
        if(timer==null){
            timer = new Timer("GameLoop",true);
            timer.scheduleAtFixedRate(new Loop(),10,50);
            System.out.println("Started GameLoop!");
        }
    }

    @Override
    public synchronized void stop() {
        if(timer!=null){
            timer.cancel();
            timer = null;
            System.out.println("Stopped GameLoop!");
        }

    }

    private class Loop extends TimerTask {
        @Override
        public void run() {
            if(isEnabled() && model != null){
                model.cycle();
            }
        }
    }

    @Override
    public String toString() {
        return "Main GameLoop";
    }
}
