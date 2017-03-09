package de.webtwob.model.menu;

import de.webtwob.interfaces.IJARInput;
import de.webtwob.interfaces.IJARView;
import de.webtwob.interfaces.Mode;

import java.util.ArrayList;

/**
 * @author Bennet Blessmann
 *         Created on 09.03.2017.
 */
public class ModeModel {

    private Mode mode;

    public void setMode(Mode mode){
        this.mode = mode;
    }

    /**
     * all inputs added to the model
     */
    private final ArrayList<IJARInput> inputs = new ArrayList<>();
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

    public synchronized void addInput(final IJARInput ijari) {

        inputs.add(ijari);
    }

    public Mode getMode() {

        return mode;
    }
}
