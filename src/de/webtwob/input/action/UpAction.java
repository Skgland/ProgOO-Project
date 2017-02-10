package de.webtwob.input.action;

import de.webtwob.interfaces.IJARModel;

import java.awt.event.ActionEvent;

/**
 * @author Bennet Blessmann
 * Created on 31. Jan. 2017.
 */
public class UpAction extends LinkableAction {

	@Override
	public void actionPerformed(final ActionEvent e) {
		if(model.getMode() == IJARModel.Mode.MENU) {
			model.up();
		}
	}

}
