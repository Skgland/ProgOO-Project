package de.webtwob.input.keyboard;

import com.sun.istack.internal.NotNull;
import de.webtwob.input.action.*;
import de.webtwob.interfaces.IJARInput;
import de.webtwob.interfaces.IJARModel;

import javax.swing.*;
import java.util.ResourceBundle;

import static de.webtwob.input.keyboard.KeyboardInput.InputActions.*;


/**
 * @author Bennet Blessmann
 * Created on 31. Jan. 2017.
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


	private IJARModel model;
	private       boolean        enabled       = true;
	private final LinkableAction JUMP_ACTION   = new JumpAction();
	private final LinkableAction SNEAK_ACTION  = new SneakAction();
	private final LinkableAction PAUSE_ACTION  = new PauseAction();
	private final LinkableAction SELECT_ACTION = new SelectAction();
	private final LinkableAction UP_ACTION     = new UpAction();
	private final LinkableAction DOWN_ACTION   = new DownAction();

	public KeyboardInput(final JComponent jc) {

		linkToMaps(jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW), jc.getActionMap());
	}

	/**
	 * inserts the necessary key-value pairs into the InputMap and the ActionMap
	 * */
	private void linkToMaps(@NotNull final InputMap imap, @NotNull final ActionMap amap) {

		final ResourceBundle keys = ResourceBundle.getBundle("de.webtwob.input.Keys");
		imap.put(KeyStroke.getKeyStroke(keys.getString("JUMP")), JUMP);
		imap.put(KeyStroke.getKeyStroke(keys.getString("SNEAK_MOD") + " " + keys.getString("JUMP")), JUMP);
		imap.put(KeyStroke.getKeyStroke(keys.getString("SNEAK_MOD") + " " + keys.getString("SNEAK")), SNEAK);
		imap.put(KeyStroke.getKeyStroke("released " + keys.getString("SNEAK")), UN_SNEAK);
		imap.put(KeyStroke.getKeyStroke(keys.getString("PAUSE")), PAUSE);

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

	@Override
	public void linkModel(final IJARModel ijarm) {

		model = ijarm;
		if (enabled) {
			linkActions();
		}
	}

	private void linkActions() {

		JUMP_ACTION.linkModel(model);
		SNEAK_ACTION.linkModel(model);
		PAUSE_ACTION.linkModel(model);
		SELECT_ACTION.linkModel(model);
		UP_ACTION.linkModel(model);
		DOWN_ACTION.linkModel(model);
	}

	private void unlinkActions() {

		JUMP_ACTION.linkModel(null);
		SNEAK_ACTION.linkModel(null);
		PAUSE_ACTION.linkModel(null);
		SELECT_ACTION.linkModel(null);
		UP_ACTION.linkModel(null);
		DOWN_ACTION.linkModel(null);
	}

	@Override
	public void setEnabled(final boolean enable) {

		enabled = enable;
		if (enable) {
			linkActions();
		} else {
			unlinkActions();
		}
	}

	@Override
	public boolean isEnabled() {

		return enabled;
	}

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
