package de.webtwob.input.action;

import de.webtwob.interfaces.IJARModel;

import java.awt.event.ActionEvent;

/**
 * Created by BB20101997 on 31. Jan. 2017.
 */
public class UpAction extends LinkableAction {

	@Override
	public void actionPerformed(ActionEvent e) {
		if(model.getMode() == IJARModel.Mode.MENU) {
			model.up();
		}
	}

}
