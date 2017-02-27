package de.webtwob.input.controller;

import de.webtwob.interfaces.IJARInput;
import de.webtwob.interfaces.IJARModel;
import net.java.games.input.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Bennet Blessmann
 *         Created on 01. Feb. 2017.
 */
public class ControllerInput implements IJARInput {

	//TODO check for race conditions

	private final Poller poller = new Poller();
	private           Thread     exec;
	private transient IJARModel  model;
	private transient Controller controller;
	private boolean enabled = true;

	public ControllerInput() {

		findController();
		if (controller == null) {
			enabled = false;
		}
		//does not work :(
		//ControllerEnvironment.getDefaultEnvironment().addControllerListener(new ControllerInputControllerListener(this));
	}

	@Override
	public void linkModel(final IJARModel ijarm) {

		model = ijarm;
	}
	private void findController() {

		ControllerEnvironment controllerEnvironment = ControllerEnvironment.getDefaultEnvironment();
		try {
			//horrible hack to update the list of connected controllers
			final Class       clazz = controllerEnvironment.getClass();
			final Constructor field = clazz.getConstructors()[0];
			field.setAccessible(true);
			controllerEnvironment = (ControllerEnvironment) field.newInstance();
			for (final Controller cont : controllerEnvironment.getControllers()) {
				if (cont.getType() == Controller.Type.GAMEPAD) {
					controller = cont;
					break;
				}
			}
		} catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
			e.printStackTrace();
		}
	}	@Override
	public void setEnabled(final boolean enable) {

		if (this.enabled == enable) {
			return;
		}
		if (!this.enabled && (controller == null)) {
			findController();
			if (controller == null) {
				return;
			}
		}
		synchronized (poller) {
			this.enabled = enable;
			poller.notifyAll();
		}
	}
	@Override
	public String toString() {

		return "[ControllerInput]" + (controller != null ? " connected to " + controller.getName() : " not connected!");
	}

	public class Poller implements Runnable {

		transient boolean run = true;

		@Override
		public void run() {

			final Event event = new Event();
			EventQueue  eventQueue;
			while (run) {
				if (enabled) {
					if (controller != null && model != null) {
						if (!controller.poll()) {
							controller = null;
							if (model.getMode() == IJARModel.Mode.GAME) {
								model.pause();
							}
							continue;
						}
						eventQueue = controller.getEventQueue();
						while (eventQueue.getNextEvent(event)) {
							handleEvent(event);
						}
					} else if (controller == null) {
						findController();
						if (controller == null) {
							try {
								synchronized (poller) {
									poller.wait(1000);
								}
							} catch (final InterruptedException e) {
								e.printStackTrace();
							}
						}
					}

					try {
						java.awt.EventQueue.invokeAndWait(() -> {
						});
						synchronized (poller) {
							poller.wait(10);
						}
					} catch (InterruptedException | InvocationTargetException e) {
						e.printStackTrace();
					}
				} else {
					synchronized (poller) {
						if (!enabled) {
							try {
								poller.wait();
							} catch (final InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}

		public void handleEvent(final Event event) {

			final Component.Identifier identifier;
			identifier = event.getComponent().getIdentifier();
			//noinspection IfStatementWithTooManyBranches
			if (identifier == Component.Identifier.Button._0) {
				if (event.getValue() == 1.0) {
					if (model.getMode() == IJARModel.Mode.GAME) {
						model.jump();
					} else {
						model.doSelect();
					}
				}
			} else if (identifier == Component.Identifier.Button._7) {
				if (event.getValue() == 1.0) {
					java.awt.EventQueue.invokeLater(model::pause);
				}
			} else if (identifier == Component.Identifier.Button._5) {
				//sometimes a bit unreliable, sometimes even analogue?
				model.setSneaking(event.getComponent().getPollData() == 1.0);
			} else if (identifier == Component.Identifier.Axis.POV) {
				if (event.getValue() == Component.POV.UP) {
					java.awt.EventQueue.invokeLater(model::up);
				} else if (event.getValue() == Component.POV.DOWN) {
					java.awt.EventQueue.invokeLater(model::down);
				}
			}
			/*else {
			 * 	System.out.println(event+" | "+event.getComponent().getIdentifier().getClass());
			}*/
		}

		@Override
		public String toString() {

			return "[ControllerInput]" + ((controller != null) ? "[Port]:" + controller.getPortNumber() : "");
		}
	}
	@Override
	public boolean isEnabled() {

		return enabled;
	}


	@Override
	public void start() {

		if (exec == null) {
			exec = new Thread(poller);
			exec.setName("Controller Poller");
			exec.start();
		}
		System.out.println("Started ControllerInput");
	}

	@Override
	public void stop() {

		poller.run = false;
		exec = null;
		System.out.println("Stopped ControllerInput");
	}

}
