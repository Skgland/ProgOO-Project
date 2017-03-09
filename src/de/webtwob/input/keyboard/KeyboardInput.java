package de.webtwob.input.keyboard;

import de.webtwob.input.action.*;
import de.webtwob.interfaces.IJARGameModel;
import de.webtwob.interfaces.IJARInput;
import de.webtwob.interfaces.IJARMenuModel;
import de.webtwob.model.ModeModel;

import javax.swing.*;
import java.util.ResourceBundle;

import static de.webtwob.input.keyboard.KeyboardInput.InputActions.*;


/**
 * @author Bennet Blessmann Created on 31. Jan. 2017.
 */
public class KeyboardInput implements IJARInput {


    enum InputActions {
        JUMP,
        SNEAK,
        UN_SNEAK,
        SELECT,
        PAUSE,
        UP,
        DOWN
    }

    private boolean enabled = true;
    private AbstractAction JUMP_ACTION;
    private AbstractAction SNEAK_ACTION;
    private AbstractAction PAUSE_ACTION;
    private AbstractAction SELECT_ACTION;
    private AbstractAction UP_ACTION;
    private AbstractAction DOWN_ACTION;

    /**
     * inserts the necessary key-value pairs into the InputMap and the ActionMap
     */
    private void linkToMaps(final InputMap imap, final ActionMap amap) {

        final ResourceBundle keys = ResourceBundle.getBundle("de.webtwob.input.Keys");
        imap.put(KeyStroke.getKeyStroke(keys.getString("JUMP")), JUMP);
        imap.put(KeyStroke.getKeyStroke(keys.getString("SNEAK_MOD") + " " + keys.getString("JUMP")), JUMP);
        imap.put(KeyStroke.getKeyStroke(keys.getString("SNEAK_MOD") + " " + keys.getString("SNEAK")), SNEAK);
        imap.put(KeyStroke.getKeyStroke("released " + keys.getString("SNEAK")), UN_SNEAK);
        imap.put(KeyStroke.getKeyStroke(keys.getString("PAUSE")), PAUSE);
        imap.put(KeyStroke.getKeyStroke(keys.getString("SNEAK_MOD")+" "+keys.getString("PAUSE")), PAUSE);
        imap.put(KeyStroke.getKeyStroke(keys.getString("ARROW_UP")), UP);
        imap.put(KeyStroke.getKeyStroke(keys.getString("ARROW_DOWN")), DOWN);
        imap.put(KeyStroke.getKeyStroke("ENTER"), SELECT);

        amap.put(JUMP, JUMP_ACTION);
        amap.put(SNEAK, SNEAK_ACTION);
        amap.put(UN_SNEAK, SNEAK_ACTION);
        amap.put(PAUSE, PAUSE_ACTION);
        amap.put(SELECT, SELECT_ACTION);
        amap.put(UP, UP_ACTION);
        amap.put(DOWN, DOWN_ACTION);
    }

    public KeyboardInput(final JComponent jc, final IJARGameModel gameModel, final IJARMenuModel menuModel, final
    ModeModel modeModel) {
        JUMP_ACTION = new JumpAction(modeModel, gameModel);
        SNEAK_ACTION = new SneakAction(gameModel);
        PAUSE_ACTION = new PauseAction(modeModel, menuModel);
        SELECT_ACTION = new SelectAction(modeModel, menuModel);
        UP_ACTION = new UpAction(modeModel, menuModel);
        DOWN_ACTION = new DownAction(modeModel, menuModel);

        linkToMaps(jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW), jc.getActionMap());
    }

    @Override
    public void setEnabled(final boolean enable) {
        enabled = enable;
        JUMP_ACTION.setEnabled(enable);
        SNEAK_ACTION.setEnabled(enable);
        PAUSE_ACTION.setEnabled(enable);
        SELECT_ACTION.setEnabled(enable);
        UP_ACTION.setEnabled(enable);
        DOWN_ACTION.setEnabled(enable);
    }

    @Override
    public boolean isEnabled() {

        return enabled;
    }


    /**
     * This is not an active polling input therefore the next two method's are just dummy implementations
     */
    @Override
    public void start() {

        System.out.println("Started KeyboardInput");
    }

    @Override
    public void stop() {

        System.out.println("Stopped KeyboardInput");
    }

    @Override
    public String toString() {

        return "[KeyboardInput]";
    }
}
