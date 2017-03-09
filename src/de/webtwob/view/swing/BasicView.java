package de.webtwob.view.swing;

import de.webtwob.interfaces.*;
import de.webtwob.model.ModeModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bennet Blessmann Created on 31. Jan. 2017.
 */
public class BasicView extends JPanel implements IJARView {

    private final MenuPanel menuPanel = new MenuPanel();
    private final IJARGameModel game;

    /**
     * Stores the reused GameField
     */
    private final GameField gameField;
    private          IJARMenuModel menu;
    private          ModeModel     mode;
    /**
     * The thread to update this view
     */
    private          Thread        exec;
    /**
     * if exec should continue running
     */
    private volatile boolean       running;
    /**
     * Used to store the latest selected value so we only update on change
     */
    private       int              select      = -1;
    /**
     * Stores the currently in use menuEntries
     */
    private       List<IMenuEntry> menuEntries = new ArrayList<>();
    private final Runnable         runner      = this::keepAlive;


    public BasicView(final IJARGameModel game, final IJARMenuModel menu, final ModeModel mode) {
        this.game = game;
        this.menu = menu;
        this.mode = mode;
        gameField = new GameField(game);
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
        while (running) {
            try {
                if (mode.getMode() != null) {
                    if (mode.getMode() == Mode.GAME) {
                        if (currentMode != Mode.GAME) {
                            removeAll();
                            add(gameField, BorderLayout.CENTER);
                            currentMode = Mode.GAME;
                            updateUI();
                        }
                        gameField.run();
                        synchronized (runner) {
                            runner.wait(10);
                        }
                    } else {
                        if (currentMode != Mode.MENU) {
                            removeAll();
                            add(menuPanel, BorderLayout.CENTER);
                            currentMode = Mode.MENU;
                            menuChanged();
                            updateUI();
                        }
                        if (!menuEntries.equals(menu.getMenuEntries())) {
                            menuChanged();
                        }
                        if (select != menu.getSelectedIndex()) {
                            select = menu.getSelectedIndex();
                            menuPanel.updateSelection(select);
                        }
                        synchronized (runner) {
                            runner.wait(10);
                        }
                    }
                }
            } catch (final InterruptedException ignored) {
                //expected from the waits
            } catch (final Exception e) {
                /*
                 * probably missed a null check
                 * still the view should just keep updating
                 * no matter what happens
                */
                e.printStackTrace();
            }
        }
    }

    private void menuChanged() {

        menuEntries = menu.getMenuEntries();
        menuPanel.updateMenu(menu.getCurrentMenu());
        select = menu.getSelectedIndex();
    }

    /**
     * Start the update Thread
     */
    @Override
    public synchronized void start() {

        if (exec == null) {
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
        synchronized (runner) {
            runner.notifyAll();
        }
        while (exec.isAlive()) {
            try {
                //wait for the thread to yield
                exec.join();
            } catch (final InterruptedException ignore) {
            }
        }
        exec = null;
    }

    /**
     * handel a forced update
     * usually when a MenuEntry changes it's Text
     * to get the view to update
     */
    @Override
    public void forceUpdate() {

        menuChanged();
        updateUI();
    }
}
