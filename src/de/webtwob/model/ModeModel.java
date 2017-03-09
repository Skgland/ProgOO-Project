package de.webtwob.model;

import de.webtwob.interfaces.Mode;

/**
 * @author Bennet Blessmann
 *         Created on 09.03.2017.
 */
public class ModeModel {

    private Mode mode = Mode.MENU;

    public void setMode(final Mode mode){
        this.mode = mode;
    }

    public Mode getMode() {

        return mode;
    }

    @Override
    public String toString() {
        return "ModeModel{" + "mode=" + mode + '}';
    }
}
