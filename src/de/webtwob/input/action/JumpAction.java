package de.webtwob.input.action;

import de.webtwob.interfaces.IJARGameModel;
import de.webtwob.interfaces.Mode;
import de.webtwob.model.ModeModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Bennet Blessmann
 *         Created on 31. Jan. 2017.
 */
public class JumpAction extends AbstractAction {

    private final ModeModel modeModel;
    private final IJARGameModel gameModel;

    public JumpAction(ModeModel modeModel, IJARGameModel gameModel) {
        this.modeModel = modeModel;
        this.gameModel = gameModel;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        //if in game jump
        if (modeModel.getMode() == Mode.GAME) {
            gameModel.jump();
        }
    }

}
