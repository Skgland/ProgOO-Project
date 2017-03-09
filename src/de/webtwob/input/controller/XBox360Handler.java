package de.webtwob.input.controller;

import de.webtwob.interfaces.IJARGameModel;
import de.webtwob.interfaces.IJARMenuModel;
import de.webtwob.interfaces.Mode;
import de.webtwob.model.ModeModel;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static de.webtwob.input.controller.XBox360Const.*;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickButtons;

/**
 * Created by BB20101997 on 02. Mär. 2017.
 * <p>
 * This class handels Controller input for the XBox 360 Controller
 */
public class XBox360Handler{

    private final boolean[] pressed = new boolean[4];
    private final ModeModel     mode;
    private final IJARGameModel game;
    private final IJARMenuModel menu;


    public XBox360Handler(final IJARGameModel game,final IJARMenuModel menu,final ModeModel mode){
        this.game = game;
        this.mode = mode;
        this.menu = menu;
    }

    /**
     * This function handles input from Xbox 360 controllers
     * Note: needs to be executed on the main Thread
     *
     * @param id the controller to be handled
     *           <p>
     */
    public void handleXBoxController(final int id) {
        final ByteBuffer button = glfwGetJoystickButtons(id);
        if(button.get(BUTTON_A) == 1) {
            if(mode.getMode() == Mode.GAME) {
                game.jump();
            } else {
                if(!pressed[0]) {
                    pressed[0] = true;
                    //if done directly quit causes deadlock when stopping the ControllerInput
                    EventQueue.invokeLater(menu::doSelect);
                }
            }
        } else {
            pressed[0] = false;
        }
        if(button.get(BUTTON_START) == 1) {
            mode.setMode(Mode.MENU);
            menu.pause();
        }
        if(button.get(BUTTON_DOWN) == 1) {
            if(!pressed[1]) {
                pressed[1] = true;
                menu.down();
            }
        } else {
            pressed[1] = false;
        }

        if(button.get(BUTTON_UP) == 1) {
            if(!pressed[2]) {
                pressed[2] = true;
                menu.up();
            }
        } else {
            pressed[2] = false;
        }
        final boolean sneak = button.get(BUTTON_RB) == 1;
        if(game.isSneaking() != sneak && sneak != pressed[3]) {
            game.setSneaking(button.get(BUTTON_RB) == 1);
        }
        pressed[3] = sneak;
    }

    @Override
    public String toString() {
        return "XBox360Handler{" + "pressed=" + Arrays.toString(pressed) + ", mode=" + mode + ", game=" + game + ", " +
                       "menu=" + menu + '}';
    }
}
