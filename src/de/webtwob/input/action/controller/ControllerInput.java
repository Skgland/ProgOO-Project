package de.webtwob.input.action.controller;

import de.webtwob.interfaces.IJARInput;
import de.webtwob.interfaces.IJARModel;
import net.java.games.input.*;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by BB20101997 on 01. Feb. 2017.
 */
public class ControllerInput implements IJARInput {

	private           Thread     exec;
	private           Poller     poller;
	private transient IJARModel  model;
	private transient Controller controller;

	public ControllerInput() {
		findController();
		ControllerEnvironment.getDefaultEnvironment().addControllerListener(new ControllerInputControllerListener(this));
	}

	@Override
	public void linkModel(IJARModel ijarm) {
		model = ijarm;
	}

	@Override
	public void start() {
		if(exec == null) {
			exec = new Thread(poller = new Poller());
			exec.start();
		}
	}

	public void join() throws InterruptedException {
		exec.join();
	}

	public void join(long l) throws InterruptedException {
		exec.join(l);
	}

	@Override
	public void stop() {
		poller.run = false;
		exec = null;
	}

	public void setController(Controller c) {
		controller = c;
	}

	public Controller getController() {
		return controller;
	}

	public void findController() {
		for(Controller cont : ControllerEnvironment.getDefaultEnvironment().getControllers()) {
			System.out.println(cont.getType() + ":" + cont.getName());
			if(cont.getType() == Controller.Type.GAMEPAD) {
				controller = cont;
				break;
			}
		}
	}

	public class Poller implements Runnable {

		transient boolean run = true;

		@Override
		public void run() {
			Event      event = new Event();
			EventQueue eventQueue;
			while(run) {
				if(controller != null && model != null) {
					if(!controller.poll()){
						controller = null;
						continue;
					}
					eventQueue = controller.getEventQueue();
					while(eventQueue.getNextEvent(event)) {
						handleEvent(event);
					}
				}else if(controller==null){
					findController();
				}

				try {
					java.awt.EventQueue.invokeAndWait(() -> {});
					Thread.sleep(10);
				}
				catch(InterruptedException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}

		public void handleEvent(Event event) {
			Component.Identifier identifier;
			identifier = event.getComponent().getIdentifier();
			if(identifier == Component.Identifier.Button._0) {
				if(event.getValue() == 1.0) {
					if(model.getMode() == IJARModel.Mode.GAME) {
						java.awt.EventQueue.invokeLater(model::jump);
					} else {
						java.awt.EventQueue.invokeLater(model::doSelect);
					}
				}
			} else if(identifier == Component.Identifier.Button._7) {
				if(event.getValue() == 1.0) {
					java.awt.EventQueue.invokeLater(model::pause);
				}
			} else if(identifier == Component.Identifier.Button._5) {
				java.awt.EventQueue.invokeLater(() -> model.setSneaking(Math.abs(event.getValue()) > 0.1));
			} else if(identifier == Component.Identifier.Axis.POV) {
				if(event.getValue() == Component.POV.UP) {
					java.awt.EventQueue.invokeLater(model::up);
				} else if(event.getValue() == Component.POV.DOWN) {
					java.awt.EventQueue.invokeLater(model::down);
				}
			} else {
				//System.out.println(event+" | "+event.getComponent().getIdentifier().getClass());
			}
		}

	}
}
