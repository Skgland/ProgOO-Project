package de.webtwob.input;

import de.webtwob.input.action.JumpAction;
import de.webtwob.input.action.LinkableAction;
import de.webtwob.input.action.PauseAction;
import de.webtwob.input.action.SneakAction;
import de.webtwob.interfaces.IJARInput;
import de.webtwob.interfaces.IJARModel;

import javax.swing.*;

import static de.webtwob.input.KeyboardInput.InputActions.*;


/**
 * Created by BB20101997 on 31. Jan. 2017.
 */
public class KeyboardInput implements IJARInput{

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
	private final LinkableAction JUMP_ACTION  = new JumpAction();
	private final LinkableAction SNEAK_ACTION = new SneakAction();
	private final LinkableAction PAUSE_ACTION = new PauseAction();

	public KeyboardInput(JComponent jc) {
		InputMap inputMap = jc.getInputMap();

		inputMap.put(KeyStroke.getKeyStroke("SPACE"), JUMP);
		inputMap.put(KeyStroke.getKeyStroke("shift SPACE"), JUMP);
		inputMap.put(KeyStroke.getKeyStroke("shift pressed SHIFT"), SNEAK);
		inputMap.put(KeyStroke.getKeyStroke("released SHIFT"), UN_SNEAK);
		inputMap.put(KeyStroke.getKeyStroke("P"), PAUSE);

		inputMap.put(KeyStroke.getKeyStroke("UP"), UP);
		inputMap.put(KeyStroke.getKeyStroke("DOWN"), DOWN);
		inputMap.put(KeyStroke.getKeyStroke("ENTER"), SELECT);

		ActionMap actionMap = jc.getActionMap();
		actionMap.put(JUMP, JUMP_ACTION);
		actionMap.put(SNEAK, SNEAK_ACTION);
		actionMap.put(UN_SNEAK, SNEAK_ACTION);
		actionMap.put(PAUSE, PAUSE_ACTION);
	}

	@Override
	public void linkModel(IJARModel ijarm) {
		model = ijarm;
		JUMP_ACTION.linkModel(ijarm);
		SNEAK_ACTION.linkModel(ijarm);
		PAUSE_ACTION.linkModel(ijarm);
	}

	@Override
	public void run() {
		//does not actually have to do anything
	}
}
