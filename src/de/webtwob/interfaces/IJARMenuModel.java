package de.webtwob.interfaces;

import java.util.List;

/**
 * @author Bennet Blessmann Created on 09.03.2017.
 */
public interface IJARMenuModel {

    /**
     * will cause the action associated with the current menu entry to be executed
     */
    void doSelect();

    /**
     * Selects the Menu Item with the given indicator
     * does not execute it's associated action
     *
     * @param i the chosen indicator
     */
    void select(int i);

    /**
     * move selection up one
     * it's up to the model to decide if when at the top of the menu it will stay there or
     * if up on the top entry will wrap around to the bottom
     */
    void up();

    /**
     * move selection down one
     */
    void down();

    /**
     * @return returns the menu entries of the current menu
     */
    List<IMenuEntry> getMenuEntries();

    /**
     * @return the currently active menu
     */
    IMenu getCurrentMenu();

    /**
     * @return the selected Menu Entry, default is 0
     */
    int getSelectedIndex();

    /**
     * Sets the list of currently available IJARInputs
     *
     * @param inputList the ist of IJARInputs
     */
    void setInputList(List<IJARInput> inputList);

    /**
     * does the gui need to be updated
     */
    boolean isDirty();

    /**
     * clear the update necessary flag
     */
    void clean();

    /**
     * set the update necessary flag
     */
    void setDirty();

    /**
     * set the current menu to the pause menu
     */
    void pause();

    /**
     * move to game over menu
     */
    void gameOver();
}

