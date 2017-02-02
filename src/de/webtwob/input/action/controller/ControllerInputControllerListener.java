package de.webtwob.input.action.controller;

import net.java.games.input.ControllerEvent;
import net.java.games.input.ControllerListener;

/**
 * Created by BB20101997 on 01. Feb. 2017.
 */
public class ControllerInputControllerListener implements ControllerListener {

	ControllerInput controllerInput;

	public ControllerInputControllerListener(ControllerInput ci){
		controllerInput = ci;
	}

	@Override
	public void controllerRemoved(ControllerEvent controllerEvent) {
		System.out.println("Controller Removed:"+controllerEvent.getController());
		if(controllerEvent.getController()==controllerInput.getController()){
			controllerInput.setController(null);
		}
	}

	@Override
	public void controllerAdded(ControllerEvent controllerEvent) {
		System.out.println("Controller Added:" + controllerEvent.getController());
		if(null == controllerInput.getController()) {
			controllerInput.setController(controllerEvent.getController());
		}
	}
}
