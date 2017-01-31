package de.webtwob.input.action;

import de.webtwob.interfaces.IJARLinkable;
import de.webtwob.interfaces.IJARModel;

import javax.swing.*;

/**
 * Created by BB20101997 on 31. Jan. 2017.
 */
public abstract class LinkableAction extends AbstractAction implements IJARLinkable {

	protected IJARModel model;

	public LinkableAction(){
		setEnabled(false);
	}

	@Override
	public void linkModel(IJARModel ijarm) {
		model = ijarm;
		setEnabled(model!=null);
	}
}
