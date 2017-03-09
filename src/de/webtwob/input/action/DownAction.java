package de.webtwob.input.action;

import de.webtwob.interfaces.IJARMenuModel;
import de.webtwob.interfaces.Mode;
import de.webtwob.model.ModeModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Bennet Blessmann
 * Created on 31. Jan. 2017.
 */
public class DownAction extends AbstractAction {

    private final ModeModel modeModel;
    private final IJARMenuModel menuModel;

    public DownAction(final ModeModel modeModel, final IJARMenuModel menuModel) {

        this.modeModel = modeModel;
        this.menuModel = menuModel;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        //if in menu perform down action
        if(modeModel.getMode() == Mode.MENU) {
            menuModel.down();
        }
    }

}
