package de.webtwob.input.gameloop;

import de.webtwob.interfaces.IJARGameModel;
import de.webtwob.interfaces.IJARInput;
import de.webtwob.interfaces.IJARMenuModel;
import de.webtwob.interfaces.Mode;
import de.webtwob.model.ModeModel;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by BB20101997 on 08. Mär. 2017.
 */
public class GameLoop implements IJARInput {

    private final ModeModel     mode;
    private final IJARGameModel game;
    private final IJARMenuModel menu;
    private       Timer         timer;
    private boolean enabled = true;

    public GameLoop(final ModeModel mode, final IJARGameModel game, final IJARMenuModel menu) {

        this.menu = menu;
        this.game = game;
        this.mode = mode;
    }

    @Override
    public String toString() {

        return "Main GameLoop";
    }

    private class Loop extends TimerTask {

        @Override
        public void run() {

            if(isEnabled() && mode != null) {
                if(mode.getMode() == Mode.GAME) {
                    if(!game.cycle()) {
                        mode.setMode(Mode.MENU);
                        menu.gameover();
                    }
                }
            }
        }
    }

    @Override
    public void setEnabled(final boolean enable) {

        enabled = enable;
    }


    @Override
    public boolean isEnabled() {

        return enabled;
    }

    @Override
    public synchronized void start() {

        if(timer == null) {
            timer = new Timer("GameLoop", true);
            timer.scheduleAtFixedRate(new Loop(), 10, 50);
            System.out.println("Started GameLoop!");
        }
    }

    @Override
    public synchronized void stop() {

        if(timer != null) {
            timer.cancel();
            timer = null;
            System.out.println("Stopped GameLoop!");
        }

    }


}
