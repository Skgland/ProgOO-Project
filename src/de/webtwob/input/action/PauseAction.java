package de.webtwob.input.action;

import de.webtwob.interfaces.IJARMenuModel;
import de.webtwob.interfaces.Mode;
import de.webtwob.model.ModeModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Bennet Blessmann
 *         Created on 01. Feb. 2017.
 */
public class PauseAction extends AbstractAction {

    private final ModeModel     modeModel;
    private final IJARMenuModel menuModel;

    public PauseAction(final ModeModel modeModel, final IJARMenuModel menuModel) {

        this.modeModel = modeModel;
        this.menuModel = menuModel;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {

        if (modeModel.getMode() == Mode.GAME) {
            modeModel.setMode(Mode.MENU);
            menuModel.pause();
        }
    }

    @Override
    public String toString() {
        return "PauseAction{" + "modeModel=" + modeModel + ", menuModel=" + menuModel + '}';
    }
}
