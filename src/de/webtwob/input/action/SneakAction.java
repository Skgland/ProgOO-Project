package de.webtwob.input.action;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;

/**
 * Created by BB20101997 on 31. Jan. 2017.
 */
public class SneakAction extends LinkableAction {

	@Override
	public void actionPerformed(ActionEvent e) {
		boolean sneak = (e.getModifiers() & InputEvent.SHIFT_MASK) != 0;
		if(model.isSneaking() != sneak) {
			model.setSneaking(sneak);
		}
	}

}
