package de.webtwob.input.controller;

import de.webtwob.interfaces.IJARInput;
import de.webtwob.interfaces.IJARModel;
import org.lwjgl.glfw.GLFW;

import static de.webtwob.input.controller.XBox360Const.NAME;
import static org.lwjgl.glfw.GLFW.*;

/**
 * @author Bennet Blessmann Created on 01. Feb. 2017.
 */
public class ControllerInput implements IJARInput {

    private final Poller poller = new Poller();
    private           Thread    exec;
    private transient IJARModel model;
    private boolean enabled     = true;
    private int     joystick_id = -1;

    private XBox360Handler xBox360Handler = new XBox360Handler();

    public ControllerInput() {

        if(!glfwInit()) {
            throw new InternalError("Failed to Initialize glfw!");
        }

        glfwSetJoystickCallback(this::joystick_callback);

        findJoystick();

        if(!glfwJoystickPresent(joystick_id)) {
            enabled = false;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(GLFW::glfwTerminate));
    }

    private void findJoystick() {
        for(int i = GLFW_JOYSTICK_1; i <= GLFW_JOYSTICK_LAST; i++) {
            if(glfwJoystickPresent(i)) {
                switch(glfwGetJoystickName(i)) {
                    case NAME: {
                        joystick_id = i;
                        System.out.println(NAME);
                        return;
                    }
                }
            }
        }
        System.out.println("No compatible Joystick found!");
    }

    private void joystick_callback(int id, int event) {
        if(id == joystick_id && event == GLFW_DISCONNECTED) {
            if(model.getMode() == IJARModel.Mode.GAME) { model.pause(); }
            joystick_id = -1;
            System.out.println("Joystick disconnected!");
        }
        if(event == GLFW_CONNECTED) {
            if(-1 == joystick_id) {
                synchronized(poller){
                    poller.notifyAll();
                }
                findJoystick();
            }
            System.out.println("Joystick connected!");
        }
    }

    @Override
    public void linkModel(final IJARModel ijarm) {

        model = ijarm;
        xBox360Handler.linkModel(ijarm);
        if(enabled && model != null) {
            synchronized(poller) {
                poller.notifyAll();
            }
        }
    }

    @Override
    public void setEnabled(final boolean enable) {

        if(enabled == enable) {
            return;
        }
        if(!enabled && !glfwJoystickPresent(joystick_id)) {
            //if disabled and no controller stay disabled
            return;
        }
        enabled = enable;
        if(enabled && model != null) {
            synchronized(poller) {
                poller.notifyAll();
            }
        }
    }

    @Override
    public String toString() {

        return "[ControllerInput]" + (glfwJoystickPresent(joystick_id) ? " connected to " + GLFW.glfwGetJoystickName(joystick_id) : " not connected!");
    }

    private class Poller implements Runnable {

        transient boolean run = true;

        @Override
        public void run() {

            while(run) {
                try {
                    if(enabled && model != null) {
                        glfwPollEvents();
                        if(glfwJoystickPresent(joystick_id)) {
                            if(NAME.equals(glfwGetJoystickName(joystick_id))) {
                                xBox360Handler.handleXBoxController();
                            }
                        } else {
                            synchronized(poller){
                                poller.wait();
                            }
                        }
                    } else {
                        synchronized(poller) {
                            if(!enabled || model == null) {
                                poller.wait();
                            }
                        }
                    }
                }
                catch(final NullPointerException | InterruptedException ignore) {
                    //NullPointer can occur if model is set to null after the null check by another Thread
                }
            }
        }

        @Override
        public String toString() {

            return "[Poller]";
        }
    }

    @Override
    public boolean isEnabled() {

        return enabled;
    }


    @Override
    public synchronized void start() {
        if(exec == null) {
            exec = new Thread(poller);
            exec.setName("Controller Poller");
            exec.setDaemon(true);
            exec.start();
            System.out.println("Started ControllerInput");
        } else {
            System.out.println("ControllerInput already running!");
        }
    }

    @Override
    public synchronized void stop() {
        if(exec != null) {
            poller.run = false;
            synchronized(poller) {
                poller.notifyAll();
            }
            while(exec.isAlive()) {
                try {
                    exec.join();
                }
                catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
            exec = null;
            System.out.println("Stopped ControllerInput");
        } else {
            System.out.println("ControllerInput was not running!");
        }
    }

}
