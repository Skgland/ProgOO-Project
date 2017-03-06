package de.webtwob.input.controller;

import de.webtwob.interfaces.IJARLinkable;
import de.webtwob.interfaces.IJARModel;

import java.awt.*;
import java.nio.ByteBuffer;

import static de.webtwob.input.controller.XBox360Const.*;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickButtons;

/**
 * Created by BB20101997 on 02. Mär. 2017.
 * <p>
 * This class handels Controller input for the XBox 360 Controller
 */
public class XBox360Handler implements IJARLinkable {

    private final boolean[] pressed = new boolean[4];
    private IJARModel model;

    /**
     * This function handles input from Xbox 360 controllers
     * Note: needs to be executed on the main Thread
     *
     * @param id the controller to be handled
     *           <p>
     */
    public void handleXBoxController(int id) {
        ByteBuffer button = glfwGetJoystickButtons(id);
        if(button.get(BUTTON_A) == 1) {
            if(model.getMode() == IJARModel.Mode.GAME) {
                model.jump();
            } else {
                if(!pressed[0]) {
                    pressed[0] = true;
                    //if done directly quit causes deadlock when stopping the ControllerInput
                    EventQueue.invokeLater(model::doSelect);
                }
            }
        } else {
            pressed[0] = false;
        }
        if(button.get(BUTTON_START) == 1) {
            model.pause();
        }
        if(button.get(BUTTON_DOWN) == 1) {
            if(!pressed[1]) {
                pressed[1] = true;
                model.down();
            }
        } else {
            pressed[1] = false;
        }

        if(button.get(BUTTON_UP) == 1) {
            if(!pressed[2]) {
                pressed[2] = true;
                model.up();
            }
        } else {
            pressed[2] = false;
        }
        final boolean sneak = button.get(BUTTON_RB) == 1;
        if(model.isSneaking() != sneak && sneak != pressed[3]) {
            model.setSneaking(button.get(BUTTON_RB) == 1);
        }
        pressed[3] = sneak;
    }

    @Override
    public void linkModel(final IJARModel ijarm) {
        model = ijarm;
    }
}
