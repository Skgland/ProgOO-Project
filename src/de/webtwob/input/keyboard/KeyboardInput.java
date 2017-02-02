package de.webtwob.input.keyboard;

import com.sun.istack.internal.NotNull;
import de.webtwob.input.action.*;
import de.webtwob.interfaces.IJARInput;
import de.webtwob.interfaces.IJARModel;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ResourceBundle;

import static de.webtwob.input.keyboard.KeyboardInput.InputActions.*;


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
	private final LinkableAction SELECT_ACTION = new SelectAction();
	private final LinkableAction UP_ACTION = new UpAction();
	private final LinkableAction DOWN_ACTION = new DownAction();

	public KeyboardInput(JComponent jc) {
		linkToMaps(jc.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW), jc.getActionMap());
		jc.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				System.out.println(e);
			}

			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println(e);
			}

			@Override
			public void keyReleased(KeyEvent e) {
				System.out.println(e);
			}
		});
	}

	private void linkToMaps(@NotNull InputMap imap,@NotNull ActionMap amap){
		ResourceBundle keys =  ResourceBundle.getBundle("de.webtwob.input.Keys");
		imap.put(KeyStroke.getKeyStroke(keys.getString("JUMP")), JUMP);
		imap.put(KeyStroke.getKeyStroke(keys.getString("SNEAK_MOD")+" "+keys.getString("JUMP")), JUMP);
		imap.put(KeyStroke.getKeyStroke(keys.getString("SNEAK_MOD") + " " +keys.getString("SNEAK")), SNEAK);
		imap.put(KeyStroke.getKeyStroke("released " + keys.getString("SNEAK")), UN_SNEAK);
		imap.put(KeyStroke.getKeyStroke(keys.getString("PAUSE")), PAUSE);

		imap.put(KeyStroke.getKeyStroke(keys.getString("ARROW_UP")), UP);
		imap.put(KeyStroke.getKeyStroke(keys.getString("ARROW_DOWN")), DOWN);
		imap.put(KeyStroke.getKeyStroke("ENTER"), SELECT);

		amap.put(JUMP, JUMP_ACTION);
		amap.put(SNEAK, SNEAK_ACTION);
		amap.put(UN_SNEAK, SNEAK_ACTION);
		amap.put(PAUSE, PAUSE_ACTION);
		amap.put(SELECT,SELECT_ACTION);
		amap.put(UP,UP_ACTION);
		amap.put(DOWN,DOWN_ACTION);
	}

	@Override
	public void linkModel(IJARModel ijarm) {
  		model = ijarm;
		JUMP_ACTION.linkModel(ijarm);
		SNEAK_ACTION.linkModel(ijarm);
		PAUSE_ACTION.linkModel(ijarm);
		SELECT_ACTION.linkModel(ijarm);
		UP_ACTION.linkModel(ijarm);
		DOWN_ACTION.linkModel(ijarm);
	}

	@Override
	public void start() {
		System.out.println("Started KeyboardInput");
	}

	@Override
	public void stop() {
		System.out.println("Stopped KeyboardInput");
	}
}
