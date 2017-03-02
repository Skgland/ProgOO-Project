package de.webtwob.view.swing;

import de.webtwob.interfaces.IJARModel;
import de.webtwob.interfaces.IJARView;
import de.webtwob.interfaces.IMenuEntry;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bennet Blessmann Created on 31. Jan. 2017.
 */
public class BasicView extends JPanel implements IJARView {

    /**
     * The linked Model
     */
    private IJARModel model;

    /**
     * The thread to update this view
     */
    private Thread exec;

    /**
     * if exec should continue running
     */
    private volatile boolean running;

    /**
     * Used to synchronise on
     */
    private final Object waiter = new Object();

    /**
     * Used to store the latest selected value so we only update on change
     */
    private int select = -1;

    /**
     * Stores the currently in use menuEntries
     */
    private       List<IMenuEntry> menuEntries = new ArrayList<>();
    /**
     * Stores the reused GameField
     */
    private final GameField        gameField   = new GameField();
    private final MenuPanel        menuPanel   = new MenuPanel();

    @Override
    public String toString() {
        return "BasicView " + (model != null ? "linked to " + model : "not linked to a Model.");
    }


    public BasicView() {
        setLayout(new BorderLayout());
        add(menuPanel);
        setMinimumSize(getPreferredSize());
        setVisible(true);
        updateUI();
    }

    private final Runnable runner = this::keepAlive;

    private void keepAlive() {
        IJARModel.Mode mode = null;
        while(running) {
            try {
                if(model != null && model.getMode() != null) {
                    if(model.getMode() == IJARModel.Mode.GAME) {
                        if(mode != IJARModel.Mode.GAME) {
                            removeAll();
                            add(gameField, BorderLayout.CENTER);
                            mode = IJARModel.Mode.GAME;
                            updateUI();
                        }
                        gameField.run();
                        synchronized(runner) {
                            runner.wait(10);
                        }
                    } else {
                        if(mode != IJARModel.Mode.MENU) {
                            removeAll();
                            add(menuPanel, BorderLayout.CENTER);
                            mode = IJARModel.Mode.MENU;
                            menuChanged();
                            updateUI();
                        }
                        if(!menuEntries.equals(model.getMenuEntries())) {
                            menuChanged();
                        }
                        if(select != model.getSelectedIndex()) {
                            select = model.getSelectedIndex();
                            menuPanel.updateSelection(select);
                        }
                        synchronized(runner) {
                            runner.wait(10);
                        }
                    }
                } else {
                    synchronized(runner) {
                        runner.wait(100);
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

    private void menuChanged() {
        menuEntries = model.getMenuEntries();
        menuPanel.updateMenu(model.getCurrentMenu());
        select = model.getSelectedIndex();
    }

    @Override
    public void linkModel(final IJARModel ijarm) {
        model = ijarm;
        gameField.linkModel(model);
    }

    /**
     * Start the update Thread
     */
    @Override
    public void start() {
        if(exec == null) {
            running = true;
            exec = new Thread(runner);
            exec.setName("Basic-View-Updater");
            exec.start();
        }
    }

    /**
     * stop the update Thread
     */
    @Override
    public void stop() {
        running = false;
        synchronized(runner) {
            runner.notifyAll();
        }
        try {
            //wait for the thread to yield
            exec.join();
        }
        catch(final InterruptedException ignore) {
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
