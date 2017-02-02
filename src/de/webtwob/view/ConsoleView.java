package de.webtwob.view;

import de.webtwob.interfaces.IJARModel;
import de.webtwob.interfaces.IJARView;

/**
 * Created by BB20101997 on 31. Jan. 2017.
 */
public class ConsoleView implements IJARView {

	private IJARModel model;

	@Override
	public void linkModel(IJARModel ijarm) {
		model = ijarm;
	}

	@Override
	public void start() {

	}

	@Override
	public void stop() {

	}
}
