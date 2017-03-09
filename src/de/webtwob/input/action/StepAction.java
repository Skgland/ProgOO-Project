package de.webtwob.input.action;

import de.webtwob.interfaces.IJARGameModel;
import de.webtwob.interfaces.IJARMenuModel;
import de.webtwob.interfaces.Mode;
import de.webtwob.model.ModeModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by BB20101997 on 09. Mär. 2017.
 */
public class StepAction extends AbstractAction {

    private ModeModel     mode;
    private IJARGameModel game;
    private IJARMenuModel menu;

    public StepAction(ModeModel modeModel, IJARGameModel gameModel, IJARMenuModel menuModel) {
        mode = modeModel;
        game = gameModel;
        menu = menuModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(mode.getMode() == Mode.GAME) {
            if(!game.cycle()) {
                mode.setMode(Mode.MENU);
                menu.gameover();
            }
        }
    }
}
