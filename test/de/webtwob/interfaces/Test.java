package de.webtwob.interfaces;

import de.webtwob.input.action.controller.ControllerInput;

/**
 * Created by BB20101997 on 01. Feb. 2017.
 */
public class Test {

	public static void main(String[] tArgs) {
		ControllerInput xboxInput = new ControllerInput();
		xboxInput.start();
		xboxInput.linkModel(new TestModel());
		try {
			xboxInput.join();
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}

	}

}
