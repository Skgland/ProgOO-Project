package de.webtwob.input.controller;

import de.webtwob.interfaces.IJARLinkable;
import de.webtwob.interfaces.IJARModel;

import java.nio.ByteBuffer;

import static de.webtwob.input.controller.XBox360Const.*;
import static org.lwjgl.glfw.GLFW.GLFW_JOYSTICK_1;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickButtons;

/**
 * Created by BB20101997 on 02. Mär. 2017.
 */
public class XBox360Handler implements IJARLinkable{

    boolean[] pressed = new boolean[3];
    ByteBuffer button;
    IJARModel model;

    public  void handleXBoxController(){
        button = glfwGetJoystickButtons(GLFW_JOYSTICK_1);
        if(button.get(BUTTON_A) == 1) {
            if(model.getMode() == IJARModel.Mode.GAME) {
                model.jump();
            } else {
                if(!pressed[0]) {
                    pressed[0] = true;
                    model.doSelect();
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
        if(!model.isSneaking() == (button.get(BUTTON_RB) == 1)) {
            model.setSneaking(button.get(BUTTON_RB) == 1);
        }
    }

    @Override
    public void linkModel(IJARModel ijarm) {
        model = ijarm;
    }
}
