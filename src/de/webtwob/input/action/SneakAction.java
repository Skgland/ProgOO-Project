package de.webtwob.input.action;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;

/**
 * @author Bennet Blessmann
 * Created on 31. Jan. 2017.
 */
public class SneakAction extends LinkableAction {

    @Override
    public void actionPerformed(final ActionEvent e) {
        final boolean sneak = (e.getModifiers() & InputEvent.SHIFT_MASK) != 0;
        if(model.isSneaking() != sneak) {
            model.setSneaking(sneak);
        }
    }

}
