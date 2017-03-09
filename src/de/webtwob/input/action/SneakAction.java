package de.webtwob.input.action;

import de.webtwob.interfaces.IJARGameModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;

/**
 * @author Bennet Blessmann
 * Created on 31. Jan. 2017.
 */
public class SneakAction extends AbstractAction {

    private final IJARGameModel gameModel;

    public SneakAction(final IJARGameModel gameModel) {
        this.gameModel = gameModel;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final boolean sneak = (e.getModifiers() & InputEvent.SHIFT_MASK) != 0;
        if(gameModel.isSneaking() != sneak) {
            gameModel.setSneaking(sneak);
        }
    }

    @Override
    public String toString() {
        return "SneakAction{" + "gameModel=" + gameModel + '}';
    }
}
