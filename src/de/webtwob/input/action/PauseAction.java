package de.webtwob.input.action;

import java.awt.event.ActionEvent;

/**
 * Created by BB20101997 on 01. Feb. 2017.
 */
public class PauseAction extends LinkableAction{
	@Override
	public void actionPerformed(ActionEvent e) {
		model.pause();
	}
}
