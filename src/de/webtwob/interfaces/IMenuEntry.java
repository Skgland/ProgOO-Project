package de.webtwob.interfaces;

/**
 * @author Bennet Blessmann Created on 02. Feb. 2017.
 */
public interface IMenuEntry {

    /**
     * @return the name to display for this menu entry
     */
    String getText();

    /**
     * @return whether this entry is active
     */
    boolean isActive();

    /**
     * @param active set if in general this entry should be active, even if set to true may still return false in
     *               isActive if more conditions apply
     */
    void setActive(boolean active);

    /**
     * the action to be executed if this entry is selected in a menu
     */
    void executeAction();

    /**
     * MenuEntries may have a value that value should be returned here as a String
     */
    default String getValue() {return null;}

    /**
     * if the MenuEntry has a value associated whit it this may change the current value
     */
    default void setValue(final String update) {}

}
