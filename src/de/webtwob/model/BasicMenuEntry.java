package de.webtwob.model;

import de.webtwob.interfaces.IMenuEntry;

/**
 * Created by BB20101997 on 02. Feb. 2017.
 */
public class BasicMenuEntry implements IMenuEntry{

	Runnable run;
	String name;
	boolean active;

	public BasicMenuEntry(Runnable r, String n, boolean a) {
		run = r;
		name = n;
		active = a;
	}

	@Override
	public String getText() {
		return name;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void executeAction() {
		run.run();
	}
}
