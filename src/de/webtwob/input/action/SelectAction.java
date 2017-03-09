package de.webtwob.input.action;

import java.awt.event.ActionEvent;

/**
 * @author Bennet Blessmann
 * Created on 02. Feb. 2017.
 */
public class SelectAction extends LinkableAction {

    @Override
    public void actionPerformed(final ActionEvent e) {
        if(model.getMode() == IJARModel.Mode.MENU) {
            model.doSelect();
        }
    }
}

