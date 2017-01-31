package de.webtwob.input;

import de.webtwob.input.action.JumpAction;
import de.webtwob.input.action.LinkableAction;
import de.webtwob.input.action.PauseAction;
import de.webtwob.input.action.SneakAction;
import de.webtwob.interfaces.IJARInput;
import de.webtwob.interfaces.IJARModel;

import javax.swing.*;

/**
 * Created by BB20101997 on 31. Jan. 2017.
 */
public class KeyboardInput implements IJARInput {

	private IJARModel model;
	private final LinkableAction JUMP_ACTION  = new JumpAction();
	private final LinkableAction SNEAK_ACTION = new SneakAction();
	private final LinkableAction PAUSE_ACTION = new PauseAction();

	public KeyboardInput(JComponent jc) {
		InputMap inputMap = jc.getInputMap();

		inputMap.put(KeyStroke.getKeyStroke(InputToken.JUMP), InputToken.JUMP);
		inputMap.put(KeyStroke.getKeyStroke(InputToken.SNEAK), InputToken.SNEAK);
		inputMap.put(KeyStroke.getKeyStroke(InputToken.UN_SNEAK), InputToken.UN_SNEAK);
		inputMap.put(KeyStroke.getKeyStroke(InputToken.PAUSE), InputToken.PAUSE);

		inputMap.put(KeyStroke.getKeyStroke(InputToken.UP), InputToken.UP);
		inputMap.put(KeyStroke.getKeyStroke(InputToken.DOWN), InputToken.DOWN);
		inputMap.put(KeyStroke.getKeyStroke(InputToken.SELECT), InputToken.SELECT);

		ActionMap actionMap = jc.getActionMap();
		actionMap.put(InputToken.JUMP, JUMP_ACTION);
		actionMap.put(InputToken.SNEAK, SNEAK_ACTION);
		actionMap.put(InputToken.UN_SNEAK, SNEAK_ACTION);
		actionMap.put(InputToken.PAUSE, PAUSE_ACTION);

		linkModel(null);//To disable the actions
	}

	@Override
	public void linkModel(IJARModel ijarm) {
		model = ijarm;
		JUMP_ACTION.linkModel(ijarm);
		SNEAK_ACTION.linkModel(ijarm);
		PAUSE_ACTION.linkModel(ijarm);
	}
}
