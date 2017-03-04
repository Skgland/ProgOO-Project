package de.webtwob.input.controller;

import de.webtwob.interfaces.IJARInput;
import de.webtwob.interfaces.IJARModel;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.*;

/**
 * @author Bennet Blessmann Created on 01. Feb. 2017.
 */
public class ControllerInput implements IJARInput {

    private final Poller poller = new Poller();
    private           Thread    exec;
    private transient IJARModel model;
    private boolean enabled = true;

    private XBox360Handler xBox360Handler = new XBox360Handler();

    public ControllerInput() {

        if(!glfwInit()) {
            throw new InternalError("Failed to Initialize glfw!");
        }
        if(!glfwJoystickPresent(GLFW_JOYSTICK_1)) {
            enabled = false;
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                glfwTerminate();
            }
        });

        System.out.println(GLFW.glfwGetJoystickName(GLFW_JOYSTICK_1));
    }

    @Override
    public void linkModel(final IJARModel ijarm) {

        model = ijarm;
        xBox360Handler.linkModel(ijarm);
    }

    @Override
    public void setEnabled(final boolean enable) {

        if(this.enabled == enable) {
            return;
        }
        if(!this.enabled && !glfwJoystickPresent(GLFW_JOYSTICK_1)) {
            //if disabled and no controller stay disabled
            return;
        }
        this.enabled = enable;
    }

    @Override
    public String toString() {

        return "[ControllerInput]" + (glfwJoystickPresent(GLFW_JOYSTICK_1) ? " connected to " + GLFW.glfwGetJoystickName(GLFW_JOYSTICK_1) : " not connected!");
    }

    public class Poller implements Runnable {

        transient boolean run = true;

        @Override
        public void run() {


            while(run) {
                if(enabled && model != null) {
                    glfwPollEvents();
                    if(glfwJoystickPresent(GLFW_JOYSTICK_1)) {
                        if("Xbox 360 Controller".equals(glfwGetJoystickName(GLFW_JOYSTICK_1))) {
                            xBox360Handler.handleXBoxController();
                        }
                    } else {
                        if(model.getMode() == IJARModel.Mode.GAME) { model.pause(); }
                    }
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
            exec.start();
            System.out.println("Started ControllerInput");
        }else{
            System.out.println("ControllerInput already running!");
        }
    }

    @Override
    public synchronized void stop() {
        if(exec != null) {
            poller.run = false;
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
        }else{
            System.out.println("ControllerInput was not running!");
        }
    }

}
