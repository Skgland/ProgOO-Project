package de.webtwob.interfaces;

import java.util.List;

/**
 * @author Bennet Blessmann Created on 03.02.2017.
 */
public interface IMenu extends IMenuEntry {

    /**
     * @param entry the entry to be added
     * @param index the index where the entry shall be placed
     */
    void add(IMenuEntry entry, int index);

    /**
     * Adds an IMenuItem to the end of the menu
     *
     * @param entry the item to be added
     */
    default void add(final IMenuEntry entry) {
        add(entry, size());
    }

    /**
     * @return the currents menus entry count
     */
    int size();

    /**
     * @param i the index of the entry to be returned
     *
     * @return the entry at position i
     */
    IMenuEntry get(int i);

    /**
     * @return a list of all entries
     */
    List<IMenuEntry> getEntries();


    /**
     * @param r the action that shall be executed by executeAction
     */
    void setAction(Runnable r);

}
