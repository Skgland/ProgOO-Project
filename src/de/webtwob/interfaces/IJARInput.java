package de.webtwob.interfaces;

/**
 * @author Bennet Blessmann
 * Created on 31. Jan. 2017.
 */
public interface IJARInput extends IJARRunable {

    /**
     * @param enable Enables/Disables this input
     * */
    void setEnabled(boolean enable);

    /**
     * @return if this input is enabled
     * */
    boolean isEnabled();

    /**
     * Starts this input if it's actively polling
     * */
    default void start() {

    }

    /**
     * Stops an actively polling input
     * */
    default void stop() {

    }

}
