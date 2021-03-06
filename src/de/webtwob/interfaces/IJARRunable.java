package de.webtwob.interfaces;

/**
 * @author Bennet Blessmann Created on 01. Feb. 2017.
 */
public interface IJARRunable {

    /**
     * if this component is active should start itself in a new Thread if none is already running
     */
    void start();

    /**
     * if this component is active and running at the moment should stop itself
     */
    void stop();

}
