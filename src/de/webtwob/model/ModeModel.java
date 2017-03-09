package de.webtwob.model;

import de.webtwob.interfaces.IJARView;
import de.webtwob.interfaces.Mode;

import java.util.ArrayList;

/**
 * @author Bennet Blessmann
 *         Created on 09.03.2017.
 */
public class ModeModel {

    private Mode mode = Mode.MENU;

    public void setMode(Mode mode){
        this.mode = mode;
    }

    /**
     * all views added to this model
     */
    private final ArrayList<IJARView>  views  = new ArrayList<>();


    private void pause(){
        mode = Mode.MENU;
    }

    public synchronized void addView(final IJARView ijarv) {

        views.add(ijarv);
    }

    public Mode getMode() {

        return mode;
    }
}
