package de.webtwob.view.swing;

import de.webtwob.interfaces.IJARGameModel;
import de.webtwob.interfaces.IJARMenuModel;
import de.webtwob.interfaces.IJARView;
import de.webtwob.interfaces.Mode;
import de.webtwob.model.ModeModel;

import javax.swing.*;
import java.awt.*;

/**
 * @author Bennet Blessmann Created on 31. Jan. 2017. This class switches between displaying a GameField and a MenuPanel
 *         to show the game in every mode
 */
public class BasicView extends JPanel implements IJARView {

    private final MenuPanel menuPanel;

    /**
     * Stores the reused GameField
     */
    private final    GameField gameField;
    private final    ModeModel mode;
    /**
     * The thread to update this view
     */
    private          Thread    exec;
    /**
     * if exec should continue running
     */
    private volatile boolean   running;
    private final Runnable runner = this::keepAlive;


    public BasicView(final IJARGameModel game, final IJARMenuModel menu, final ModeModel mode) {
        this.mode = mode;
        gameField = new GameField(game);
        menuPanel = new MenuPanel(menu);
        setLayout(new BorderLayout());
        add(menuPanel);
        setMinimumSize(getPreferredSize());
        setVisible(true);
        updateUI();
    }

    @Override
    public String toString() {

        return "BasicView ";
    }

    private void keepAlive() {

        Mode currentMode = null;
        while(running) {
            try {
                if(mode.getMode() != null) {
                    if(mode.getMode() == Mode.GAME) {
                        if(currentMode != Mode.GAME) {
                            removeAll();
                            add(gameField, BorderLayout.CENTER);
                            currentMode = Mode.GAME;
                            updateUI();
                        }
                        gameField.run();
                    } else {
                        if(currentMode != Mode.MENU) {
                            removeAll();
                            add(menuPanel, BorderLayout.CENTER);
                            currentMode = Mode.MENU;
                        }
                        menuPanel.updateMenu();
                    }
                    synchronized(runner) {
                        runner.wait(10);
                    }
                }
            }
            catch(final InterruptedException ignored) {
                //expected from the waits
            }
            catch(final Exception e) {
                /*
                 * probably missed a null check
                 * still the view should just keep updating
                 * no matter what happens
                */
                e.printStackTrace();
            }
        }
    }

    /**
     * Start the update Thread
     */
    @Override
    public synchronized void start() {

        if(exec == null) {
            running = true;
            exec = new Thread(runner);
            exec.setName("Basic-View-Updater");
            exec.setDaemon(true);
            exec.start();
        }
    }

    /**
     * stop the update Thread
     */
    @Override
    public synchronized void stop() {

        running = false;
        synchronized(runner) {
            runner.notifyAll();
        }
        while(exec.isAlive()) {
            try {
                //wait for the thread to yield
                exec.join();
            }
            catch(final InterruptedException ignore) {
            }
        }
        exec = null;
    }
}
