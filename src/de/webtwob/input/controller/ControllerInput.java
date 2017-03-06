package de.webtwob.input.controller;

import de.webtwob.interfaces.IJARInput;
import de.webtwob.interfaces.IJARModel;
import main.GLFWQueue;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import static de.webtwob.input.controller.XBox360Const.NAME;
import static org.lwjgl.glfw.GLFW.*;

/**
 * @author Bennet Blessmann Created on 01. Feb. 2017.
 */
public class ControllerInput implements IJARInput {

    private final Poller poller = new Poller();
    private          Thread    exec;
    private volatile IJARModel model;
    private          boolean enabled     = true;
    private volatile int     joystick_id = -1;

    private final XBox360Handler xBox360Handler = new XBox360Handler();


    /**
     * Only the main Thread can create new objects,
     * because glfwInit needs to only be called from the main Thread
     */
    public ControllerInput() {
        if(!glfwInit()) {
            throw new InternalError("Failed to Initialize glfw!");
        }
        glfwSetErrorCallback(this::error_callback);
        glfwSetJoystickCallback(this::joystick_callback);
        findJoystick();
        if(!glfwJoystickPresent(joystick_id)) {
            enabled = false;
        }
        Runtime.getRuntime().addShutdownHook(new Thread(GLFW::glfwTerminate));
    }

    /**
     * Should only be called by the main Thread!
     * */
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
        joystick_id = -1;
    }

    private void error_callback(final int code, final long description) {
        switch(code) {
            case 65537: {
                System.out.println("GLFW Error not Initialized!");
                GLFWQueue.getInstance().pushEvent(GLFW::glfwInit);
                break;
            }
            default: {
                System.out.println("GLFW Error Code: " + code + "\n" + GLFWErrorCallback.getDescription(description));
            }
        }
    }

    private void joystick_callback(final int id, final int event) {
        if(event == GLFW_DISCONNECTED) {
            if(id == joystick_id) {
                if(model.getMode() == IJARModel.Mode.GAME) {
                    model.pause();
                }
                joystick_id = -1;
            }
            System.out.println("Joystick disconnected!");
        }
        if(event == GLFW_CONNECTED) {
            if(-1 == joystick_id) {
                synchronized(poller) {
                    poller.notifyAll();
                }
                GLFWQueue.getInstance().pushEventAndWait(this::findJoystick);
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
        if(!enable || enabled || (joystick_id != -1 && glfwJoystickPresent(joystick_id))) {
            enabled = enable;
        } else {
            GLFWQueue.getInstance().pushEventAndWait(() -> {
                glfwTerminate();
                glfwInit();
                findJoystick();
                if(joystick_id != -1) {
                    enabled = true;
                    if(enabled && model != null) {
                        synchronized(poller) {
                            poller.notifyAll();
                        }
                    }
                }
            });
        }
    }

    @Override
    public String toString() {
        return "[ControllerInput]" + (joystick_id != -1 ? " connected to " + GLFW.glfwGetJoystickName(joystick_id) :
                                      " not connected!");
    }

    private class Poller implements Runnable {

        transient boolean run = true;

        @Override
        public void run() {

            while(run) {
                try {
                    if(enabled && model != null) {
                        GLFWQueue.getInstance().pushEventAndWait(() -> {
                            glfwPollEvents();
                            if(joystick_id != -1 && glfwJoystickPresent(joystick_id)) {
                                if(NAME.equals(glfwGetJoystickName(joystick_id))) {
                                    xBox360Handler.handleXBoxController(joystick_id);
                                }
                            } else {
                                glfwTerminate();
                                glfwInit();
                                findJoystick();
                            }
                        });
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
                catch(final InterruptedException e) {
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
