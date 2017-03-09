package de.webtwob.input.controller;

import de.webtwob.input.action.*;
import de.webtwob.interfaces.IJARGameModel;
import de.webtwob.interfaces.IJARMenuModel;
import de.webtwob.interfaces.Mode;
import de.webtwob.model.ModeModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.nio.ByteBuffer;
import java.util.Arrays;

import static de.webtwob.input.controller.XBox360Const.*;
import static org.lwjgl.glfw.GLFW.glfwGetJoystickButtons;

/**
 * Created by BB20101997 on 02. Mär. 2017.
 * <p>
 * This class handles Controller input for the XBox 360 Controller
 */
public class XBox360Handler {

    private final boolean[] pressed = new boolean[4];
    private final ModeModel     mode;
    private final IJARGameModel game;
    private final IJARMenuModel menu;

    private AbstractAction JUMP_ACTION;
    private AbstractAction SNEAK_ACTION;
    private AbstractAction PAUSE_ACTION;
    private AbstractAction SELECT_ACTION;
    private AbstractAction UP_ACTION;
    private AbstractAction DOWN_ACTION;


    public XBox360Handler(final IJARGameModel game, final IJARMenuModel menu, final ModeModel mode) {
        this.game = game;
        this.mode = mode;
        this.menu = menu;

        JUMP_ACTION = new JumpAction(mode, game);
        SNEAK_ACTION = new SneakAction(game);
        PAUSE_ACTION = new PauseAction(mode, menu);
        SELECT_ACTION = new SelectAction(mode, menu);
        UP_ACTION = new UpAction(mode, menu);
        DOWN_ACTION = new DownAction(mode, menu);
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
                EventQueue.invokeLater(() -> JUMP_ACTION.actionPerformed(new ActionEvent(this, id, "JUMP")));
            } else {
                if(!pressed[0]) {
                    EventQueue.invokeLater(() -> SELECT_ACTION.actionPerformed(new ActionEvent(this, id, "SELECT")));
                }
            }
            pressed[0] = true;
        } else {
            pressed[0] = false;
        }

        if(button.get(BUTTON_START) == 1) {
            EventQueue.invokeLater(() -> PAUSE_ACTION.actionPerformed(new ActionEvent(this, id, "PAUSE")));
        }

        if(button.get(BUTTON_DOWN) == 1) {
            if(!pressed[1]) {
                pressed[1] = true;
                EventQueue.invokeLater(() -> DOWN_ACTION.actionPerformed(new ActionEvent(this, id, "DOWN")));
            }
        } else {
            pressed[1] = false;
        }

        if(button.get(BUTTON_UP) == 1) {
            if(!pressed[2]) {
                pressed[2] = true;
                EventQueue.invokeLater(() -> UP_ACTION.actionPerformed(new ActionEvent(this, id, "UP")));
            }
        } else {
            pressed[2] = false;
        }

        final boolean sneak = button.get(BUTTON_RB) == 1;
        if(game.isSneaking() != sneak && sneak != pressed[3]) {
            ActionEvent actionEvent = new ActionEvent(this, id, "SNEAK", sneak ? InputEvent.SHIFT_MASK : 0);
            EventQueue.invokeLater(() -> SNEAK_ACTION.actionPerformed(actionEvent));
        }

        pressed[3] = sneak;
    }

    @Override
    public String toString() {
        return "XBox360Handler{" + "pressed=" + Arrays.toString(pressed) + ", mode=" + mode + ", game=" + game + ", "
                       + "menu=" + menu + '}';
    }
}
