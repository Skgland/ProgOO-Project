package de.webtwob.input;

import com.sun.istack.internal.NotNull;
import de.webtwob.input.action.JumpAction;
import de.webtwob.input.action.LinkableAction;
import de.webtwob.input.action.PauseAction;
import de.webtwob.input.action.SneakAction;
import de.webtwob.interfaces.IJARInput;
import de.webtwob.interfaces.IJARModel;

import javax.swing.*;

import java.util.ResourceBundle;

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
		linkToMaps(jc.getInputMap(), jc.getActionMap());
	}

	private void linkToMaps(@NotNull InputMap imap,@NotNull ActionMap amap){
		ResourceBundle keys =  ResourceBundle.getBundle("de.webtwob.input.Keys");
		imap.put(KeyStroke.getKeyStroke(keys.getString("JUMP")), JUMP);
		imap.put(KeyStroke.getKeyStroke(keys.getString("SNEAK_MOD")+" "+keys.getString("JUMP")), JUMP);
		imap.put(KeyStroke.getKeyStroke(keys.getString("SNEAK_MOD") + " " +keys.getString("SNEAK")), SNEAK);
		imap.put(KeyStroke.getKeyStroke("released " + keys.getString("SNEAK")), UN_SNEAK);
		imap.put(KeyStroke.getKeyStroke(keys.getString("PAUSE")), PAUSE);

		imap.put(KeyStroke.getKeyStroke("UP"), UP);
		imap.put(KeyStroke.getKeyStroke("DOWN"), DOWN);
		imap.put(KeyStroke.getKeyStroke("ENTER"), SELECT);

		amap.put(JUMP, JUMP_ACTION);
		amap.put(SNEAK, SNEAK_ACTION);
		amap.put(UN_SNEAK, SNEAK_ACTION);
		amap.put(PAUSE, PAUSE_ACTION);
	}

	@Override
	public void linkModel(IJARModel ijarm) {
		model = ijarm;
		JUMP_ACTION.linkModel(ijarm);
		SNEAK_ACTION.linkModel(ijarm);
		PAUSE_ACTION.linkModel(ijarm);
	}
}
