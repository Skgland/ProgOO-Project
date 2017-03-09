package de.webtwob.interfaces;

import java.util.List;

/**
 * @author Bennet Blessmann
 *         Created on 09.03.2017.
 */
public interface IJARMenuModel {

    /**
     * will cause the action associated with the current menu entry to be executed
     * */
    void doSelect();

    /**
     * Selects the Menu Item with the given indicator
     * does not execute it's associated action
     * @param i the chosen indicator
     * */
    void select(int i);

    /**
     * When in menu move selection up one
     * it's up to the model to decide if when at the top of the menu it will stay there or
     * if up on the top entry will wrap around to the bottom
     * */
    void up();

    /**
     * When in menu move selection down one
     */
    void down();

    /**+
     * @return returns menu entries when in menu mode else an empty array
     * */
    List<IMenuEntry> getMenuEntries();

    IMenu getCurrentMenu();

    /**
     * @return the selected Menu Entry, default is 0
     * */
    int getSelectedIndex();

    void setInputList(List<IJARInput> inputList);

   boolean  isDirty();
   void clean();

   void pause();

    /**
     * move to gameover menu
     * */
    void gameover();
}

