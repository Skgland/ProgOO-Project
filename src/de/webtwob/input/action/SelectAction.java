package de.webtwob.input.action;

import de.webtwob.interfaces.IJARMenuModel;
import de.webtwob.interfaces.Mode;
import de.webtwob.model.ModeModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Bennet Blessmann Created on 02. Feb. 2017.
 */
public class SelectAction extends AbstractAction {

    private final ModeModel     modeModel;
    private final IJARMenuModel menuModel;

    public SelectAction(final ModeModel modeModel, final IJARMenuModel menuModel) {

        this.modeModel = modeModel;
        this.menuModel = menuModel;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if(modeModel.getMode() == Mode.MENU) {
            menuModel.doSelect();
        }
    }

    @Override
    public String toString() {
        return "SelectAction{" + "modeModel=" + modeModel + ", menuModel=" + menuModel + '}';
    }
}

