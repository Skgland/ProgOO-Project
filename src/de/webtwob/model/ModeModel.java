package de.webtwob.model;

import de.webtwob.interfaces.Mode;

/**
 * @author Bennet Blessmann Created on 09.03.2017. Stores if we are currently in game or in the menu
 */
public class ModeModel {

    private Mode mode = Mode.MENU;

    /**
     * @return the current game mode
     */
    public Mode getMode() {

        return mode;
    }

    /**
     * @param mode sets the current game mode
     */
    public void setMode(final Mode mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "ModeModel{" + "mode=" + mode + '}';
    }
}
