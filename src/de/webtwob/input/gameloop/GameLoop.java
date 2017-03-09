package de.webtwob.input.gameloop;

import de.webtwob.interfaces.IJARGameModel;
import de.webtwob.interfaces.IJARInput;
import de.webtwob.interfaces.IJARMenuModel;
import de.webtwob.interfaces.Mode;
import de.webtwob.model.menu.ModeModel;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by BB20101997 on 08. Mär. 2017.
 */
public class GameLoop implements IJARInput {

    private ModeModel     mode;
    private IJARGameModel game;
    private IJARMenuModel menu;
    private Timer         timer;
    private boolean enabled = true;

    public GameLoop(ModeModel mode, IJARGameModel game, IJARMenuModel menu) {

        this.menu = menu;
        this.game = game;
        this.mode = mode;
    }
    @Override
    public String toString() {

        return "Main GameLoop";
    }    @Override
    public void setEnabled(boolean enable) {

        enabled = enable;
    }

    private class Loop extends TimerTask {

        @Override
        public void run() {

            if (isEnabled() && mode != null) {
                if (mode.getMode() == Mode.GAME) {
                    if (!game.cycle()) {
                        mode.setMode(Mode.MENU);
                        menu.gameover();
                    }
                }else{
                    if(menu.isDirty()){
                        mode.updateViews();
                    }
                }
            }
        }
    }    @Override
    public boolean isEnabled() {

        return enabled;
    }

    @Override
    public synchronized void start() {

        if (timer == null) {
            timer = new Timer("GameLoop", true);
            timer.scheduleAtFixedRate(new Loop(), 10, 50);
            System.out.println("Started GameLoop!");
        }
    }

    @Override
    public synchronized void stop() {

        if (timer != null) {
            timer.cancel();
            timer = null;
            System.out.println("Stopped GameLoop!");
        }

    }




}
