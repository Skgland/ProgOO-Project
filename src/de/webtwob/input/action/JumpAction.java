package de.webtwob.input.action;

import java.awt.event.ActionEvent;

/**
 * Created by BB20101997 on 31. Jan. 2017.
 */
public class JumpAction extends LinkableAction {

	@Override
	public void actionPerformed(ActionEvent e) {
		model.jump();
	}

}
