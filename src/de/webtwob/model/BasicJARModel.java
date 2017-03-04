package de.webtwob.model;

import de.webtwob.interfaces.*;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author Bennet Blessmann Created on 02. Feb. 2017.
 */
public class BasicJARModel implements IJARModel {

    private static final double               SNEAK_HEIGHT   = 2;
    private static final double               NORMAL_HEIGHT  = 4;
    private static final double               JUMP_VELOCITY  = 2;
    /**
     * contains the update logic runnable
     */
    private final        GameLoop             gameLoop       = new GameLoop();
    private final        LinkedList<IMenu>    back           = new LinkedList<>();
    private final        IMenu                MAIN_MENU      = new BasicMenu("Main Menu");
    private final        IMenu                PAUSE_MENU     = new BasicMenu("Pause Menu");
    private final        IMenu                SETTINGS_MENU  = new BasicMenu("Settings");
    private final        IMenu                GAME_OVER_MENU = new BasicMenu("Game Over");
    private final        ArrayList<IJARInput> inputs         = new ArrayList<>();
    private final        ArrayList<IJARView>  views          = new ArrayList<>();
    private final        Random               r              = new Random();
    private final        InputMenu            INPUTS_MENU    = new InputMenu(this);
    /**
     * The Players Y Position
     */
    private              double               player_y_pos   = 0;
    /**
     * The Players Height
     */
    private              double               player_height  = NORMAL_HEIGHT;
    /**
     * The Players vertical velocity
     */
    private double player_y_velocity;
    /**
     * The GameCycles since the game was started
     */
    private long gameTime    = 0;
    /**
     * All additionally score not resulting from survival time
     */
    private long bonus_score = 0;
    /**
     * The current menu
     */
    private volatile IMenu menu;
    /**
     * The currently selected menu item
     */
    private volatile int selection = 0;

    private final    IMenuEntry BACK      = new BasicMenuEntry(() -> {
        menu = back.pop();
        selection = 0;
        updateViews();
    }, "Back");
    private final    IMenuEntry GOTO_MAIN = new BasicMenuEntry(() -> {
        menu = MAIN_MENU;
        selection = 0;
        updateViews();
    }, "Main Menu");
    /**
     * The current mode of the Model
     * GAME if currently a game is running
     * MENU if we are currently in the menu
     */
    private volatile Mode       mode      = Mode.MENU;
    private final    IMenuEntry CONTINUE  = new BasicMenuEntry(() -> {
        mode = Mode.GAME;
        synchronized(gameLoop) {
            gameLoop.notifyAll();
            updateViews();
        }
    }, "Continue");

    /**
     * Has the Model been started
     */
    private volatile boolean running = false;

    /**
     * The Thread running gameLoop
     */
    private Thread loop;
    private final IMenuEntry QUIT = new BasicMenuEntry(() -> {
        stop();
        System.exit(0);
    }, "Quit");

    @SuppressWarnings("SpellCheckingInspection")
    private       Rectangle[] rects = new Rectangle[30];
    private final IMenuEntry  START = new BasicMenuEntry(() -> {
        gameTime = 0;
        bonus_score = 0;
        player_y_pos = 0;
        player_y_velocity = 0;
        rects = new Rectangle[rects.length];

        mode = Mode.GAME;
        synchronized(gameLoop) {
            gameLoop.notifyAll();
            updateViews();
        }
    }, "Start");

    {
        //setup all menus
        MAIN_MENU.add(START);
        MAIN_MENU.add(SETTINGS_MENU);
        MAIN_MENU.add(QUIT);

        PAUSE_MENU.add(CONTINUE);
        PAUSE_MENU.add(SETTINGS_MENU);
        PAUSE_MENU.add(GOTO_MAIN);

        SETTINGS_MENU.add(INPUTS_MENU);
        SETTINGS_MENU.add(BACK);

        INPUTS_MENU.add(BACK);
        INPUTS_MENU.setAction(() -> {
            back.addFirst(menu);
            selection = 0;
            menu = INPUTS_MENU;
            updateViews();
        });

        SETTINGS_MENU.setAction(() -> {
            back.addFirst(menu);
            selection = 0;
            menu = SETTINGS_MENU;
            updateViews();
        });

        GAME_OVER_MENU.add(new BasicMenuEntry(() -> {
        }, "Score") {
            @Override
            public String getValue() {

                return getScore() + "";
            }
        });
        GAME_OVER_MENU.add(START);
        GAME_OVER_MENU.add(SETTINGS_MENU);
        GAME_OVER_MENU.add(GOTO_MAIN);

        menu = MAIN_MENU;
    }

    /**
     * Start the model if not running
     */
    @Override
    public synchronized void start() {

        //set the model running
        if(loop == null && !running) {
            running = true;
            loop = new Thread(gameLoop);
            loop.setName("Main GameLoop");
            if(views.isEmpty()) {
                loop.setDaemon(false);
                loop.start();
            } else {
                loop.start();
                for(final IJARView ijarView : views) {
                    ijarView.start();
                }
            }
            for(final IJARInput ijarInput : inputs) {
                ijarInput.start();
            }
        }
    }

    @Override
    public synchronized void stop() {
        if(running && loop != null) {
            running = false;
            mode = Mode.MENU;
            for(final IJARView ijarView : views) {
                ijarView.stop();
            }
            for(final IJARInput ijarInput : inputs) {
                ijarInput.stop();
            }
            synchronized(gameLoop) {
                loop.notifyAll();
            }
            while(loop.isAlive()) {
                try {
                    loop.join();
                }
                catch(InterruptedException ignore) {
                }
            }
            loop = null;
        }
    }

    @Override
    public long getTime() {

        return gameTime;
    }

    @Override
    public long getScore() {

        return gameTime / 10 + bonus_score;
    }

    @Override
    public synchronized void addView(final IJARView ijarv) {

        ijarv.linkModel(this);
        views.add(ijarv);
        if(running) {
            ijarv.start();
        }
    }

    @Override
    public synchronized void addInput(final IJARInput ijari) {

        ijari.linkModel(this);
        inputs.add(ijari);
        if(running) {
            ijari.start();
        }
        INPUTS_MENU.updateInputs(inputs);
    }

    @Override
    public Mode getMode() {

        return mode;
    }

    @Override
    public void jump() {

        if(player_y_pos == 0 && mode == Mode.GAME) {
            player_y_velocity = JUMP_VELOCITY;
        }
    }

    @SuppressWarnings("HardcodedLineSeparator")
    @Override
    public String toString() {

        String sb = "[BasicJARModel]:\n" + "\t[Mode]: " + mode + '\n' + "\t" + menu + '\n' + "\t[Selection]: " + selection + ":" + menu.get(selection) + "\n" + "\t[Sneaking]: " + isSneaking() + "\n" + "\t[Player-Y]: " + player_y_pos + "\n" + "\t[Time]: " + gameTime + "\n";
        return sb;
    }

    private class GameLoop implements Runnable {

        int wait = 0;

        public void run() {

            while(running) {
                while(mode == Mode.GAME) {
                    gameTime++;
                    if(player_y_pos != 0 || player_y_velocity > 0) {
                        player_y_pos += player_y_velocity;
                        player_y_velocity -= 0.5;
                    }
                    if(player_y_pos < 0) {
                        player_y_pos = 0;
                    }

                    System.arraycopy(rects, 1, rects, 0, rects.length - 1);

                    //if there was enough space since last hurdle generate new one
                    if(wait <= 0 && r.nextDouble() < 0.7) {
                        if(r.nextDouble() < 0.7) {
                            rects[rects.length - 1] = new Rectangle(0, 0, 1, r.nextInt(3) + 1);
                        } else {
                            final int y = r.nextInt(2) + 2;
                            rects[rects.length - 1] = new Rectangle(0, y, 1, 6 - y);
                        }
                        wait = 8 + r.nextInt(5);
                    } else {
                        //noinspection AssignmentToNull
                        rects[rects.length - 1] = null;
                        wait--;
                    }

                    if(rects[1] != null) {
                        if(player_y_pos >= rects[1].getY() && rects[1].getY() + rects[1].getHeight() > player_y_pos) {
                            //players hurt his feet at a hurdle
                            menu = GAME_OVER_MENU;
                            select(0);
                            mode = Mode.MENU;
                        }
                        if(player_y_pos + player_height > rects[1].getY() && rects[1].getY() + rects[1].getHeight() > player_y_pos + player_height) {
                            //player hurt his head at a hurdle
                            menu = GAME_OVER_MENU;
                            select(0);
                            mode = Mode.MENU;
                        }
                    }
                    try {
                        synchronized(this) {
                            gameLoop.wait(50);
                        }
                    }
                    catch(final InterruptedException ignore) {
                    }
                }
                wait = 0;//TODO check if pause may cause issues
                try {
                    synchronized(this) {
                        this.wait();
                    }
                }
                catch(final InterruptedException ignore) {
                }
            }
        }

        @Override
        public String toString() {

            return "GameLoop{" + "wait=" + wait + '}';
        }
    }

    @Override
    public void setSneaking(final boolean sneak) {

        player_height = sneak ? SNEAK_HEIGHT : NORMAL_HEIGHT;
    }


    @Override
    public boolean isSneaking() {

        return player_height == SNEAK_HEIGHT;
    }

    @Override
    public void pause() {

        if(mode == Mode.GAME) {
            mode = Mode.MENU;
            menu = PAUSE_MENU;
        }
    }

    @Override
    public double getPlayerY() {

        return player_y_pos;
    }

    @Override
    public double getPlayerHeight() {

        return player_height;
    }

    @Override
    public Rectangle[] getHurdles() {

        return Arrays.copyOf(rects, rects.length);
    }

    @Override
    public void doSelect() {

        if(mode == Mode.MENU && selection >= 0 && selection < menu.size()) {
            final IMenuEntry ime = menu.get(selection);
            //noinspection LawOfDemeter
            ime.executeAction();
        }
    }

    @Override
    public void select(final int i) {

        selection = i;
    }

    @Override
    public void up() {

        if(mode == Mode.MENU) {
            selection = (selection + menu.size() - 1) % menu.size();
        }
    }

    @Override
    public void down() {

        if(mode == Mode.MENU) {
            selection = (selection + 1) % menu.size();
        }

    }

    @Override
    public List<IMenuEntry> getMenuEntries() {

        return new ArrayList<>(menu.getEntries());
    }


    @Override
    public int getSelectedIndex() {

        return selection;
    }

    @Override
    public void updateViews() {

        for(final IJARView view : views) {
            view.forceUpdate();
        }
    }

}
