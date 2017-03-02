package de.webtwob.interfaces;

/**
 * @author Bennet Blessmann
 * Created on 02. Feb. 2017.
 */
public interface IMenuEntry {

    String getText();
    boolean isActive();
    void setActive(boolean active);
    void executeAction();
    default String getValue(){return null;}
    default void setValue(final String update){}

}
