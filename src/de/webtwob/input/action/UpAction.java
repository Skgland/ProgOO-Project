package de.webtwob.input.action;

import de.webtwob.interfaces.IJARMenuModel;
import de.webtwob.interfaces.Mode;
import de.webtwob.model.menu.ModeModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Bennet Blessmann
 * Created on 31. Jan. 2017.
 */
public class UpAction extends AbstractAction {

    private final ModeModel modeModel;
    private final IJARMenuModel menuModel;

    public UpAction(ModeModel modeModel, IJARMenuModel menuMode) {
        this.modeModel = modeModel;
        this.menuModel = menuMode;
    }
    @Override
    public void actionPerformed(final ActionEvent e) {
        if(modeModel.getMode() == Mode.MENU) {
            menuModel.up();
        }
    }

}
