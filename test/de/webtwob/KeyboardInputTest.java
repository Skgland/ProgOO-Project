package de.webtwob;

import de.webtwob.input.keyboard.KeyboardInput;
import de.webtwob.interfaces.IJARInput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * @author Bennet Blessmann
 * Created on 31. Jan. 2017.
 */
public class KeyboardInputTest {

	private static TestModel model;
	private static Robot     robot;
	private static final Object WAITER = new Object();

	@BeforeAll
	public static void init() {

		model = new TestModel();
		final TestView  view  = new TestView();
		final IJARInput input = new KeyboardInput(view);

		view.linkModel(model);
		input.linkModel(model);

		model.addView(view);

		final JFrame frame = new JFrame("Unit Test: KeyboardInput");
		frame.add(view);
		frame.pack();
		frame.setAutoRequestFocus(true);
		frame.setVisible(true);

		try {
			robot = new Robot();
			robot.setAutoWaitForIdle(true);
			robot.setAutoDelay(10);
		}
		catch(final AWTException e) {
			e.printStackTrace();
		}
		while(!view.hasFocus()) {
			try {
				synchronized(WAITER) {
					WAITER.wait(10);
				}
			}
			catch(final InterruptedException e) {
				e.printStackTrace();
			}
		}

		model.start();
	}

	@Test
	public void testJump() {
		robot.keyPress(KeyEvent.VK_SPACE);
		if(!model.jump) {
			robot.keyRelease(KeyEvent.VK_SPACE);
			Assertions.fail("Did not jump!");
		}
		model.jump = false;
		robot.keyRelease(KeyEvent.VK_SPACE);
		if(model.jump) {
			Assertions.fail("Did jump!");
		}
	}

	@Test
	public void testSneak() {
		robot.keyPress(KeyEvent.VK_SHIFT);
		if(!model.sneak) {
			robot.keyRelease(KeyEvent.VK_SHIFT);
			Assertions.fail("Did not start to sneak!");
		}
		robot.keyRelease(KeyEvent.VK_SHIFT);
		if(model.sneak) {
			Assertions.fail("Did not stop to sneak!");
		}
	}

	@Test
	public void testJumpWhileSneaking() {
		robot.keyPress(KeyEvent.VK_SHIFT);
		robot.keyPress(KeyEvent.VK_SPACE);
		if(!model.jump) {
			robot.keyRelease(KeyEvent.VK_SHIFT);
			robot.keyRelease(KeyEvent.VK_SPACE);
			Assertions.fail("Did not jump!");
		}
		model.jump = false;
		robot.keyRelease(KeyEvent.VK_SPACE);
		if(model.jump) {
			robot.keyRelease(KeyEvent.VK_SHIFT);
			Assertions.fail("Did jump!");
		}
		robot.keyRelease(KeyEvent.VK_SHIFT);

	}
}