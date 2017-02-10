package de.webtwob.input.action;

import de.webtwob.interfaces.IJARLinkable;
import de.webtwob.interfaces.IJARModel;

import javax.swing.*;

/**
 * Created by BB20101997 on 31. Jan. 2017.
 *
 * A LinkableAction is an Action that can be linked to a model
 */
public abstract class LinkableAction extends AbstractAction implements IJARLinkable {

	volatile IJARModel model;

	LinkableAction(){
		setEnabled(false);
	}

	@Override
	public void linkModel(final IJARModel ijarm) {
		model = ijarm;
		setEnabled(model!=null);
	}
}
