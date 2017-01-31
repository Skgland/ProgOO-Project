package de.webtwob.interfaces;

import de.webtwob.input.KeyboardInput;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by BB20101997 on 31. Jan. 2017.
 */
class KeyboardInputTest {

	static class TestModel implements IJARModel {

		@Override
		public void addView(IJARView ijarv) {

		}

		@Override
		public Mode getMode() {
			return null;
		}

		@Override
		public void jump() {
			jump = true;
		}

		@Override
		public void setSneaking(boolean bool) {
			sneak = bool;
		}

		@Override
		public boolean isSneaking() {
			return sneak;
		}

		@Override
		public void pause() {

		}

		@Override
		public void doSelect() {

		}

		@Override
		public void select(int i) {

		}

		@Override
		public void up() {

		}

		@Override
		public void down() {

		}

		@Override
		public String[] getMenuEntries() {
			return new String[0];
		}

		@Override
		public int getSelectedEntry() {
			return 0;
		}
	}

	static class TestView extends JPanel implements IJARView {

		public TestView() {
			add(new JLabel("Running Test!"));
			setVisible(true);
		}

		@Override
		public void linkModel(IJARModel ijarm) {
			//not needed here
		}
	}

	private static boolean jump, sneak, unsneak;
	private static IJARModel model;
	private static TestView  view;
	private static IJARInput input;
	private static JFrame    frame;
	private static Robot     robot;
	private static Object    waiter;

	@BeforeAll
	public static void init() {
		waiter = new Object();

		model = new TestModel();
		view = new TestView();
		input = new KeyboardInput(view);

		view.linkModel(model);
		input.linkModel(model);
		model.addView(view);

		frame = new JFrame("Unit Test: KeyboardInput");
		frame.add(view);
		frame.pack();
		try {
			robot = new Robot();
			robot.setAutoWaitForIdle(true);
			robot.setAutoDelay(10);
		}
		catch(AWTException e) {
			e.printStackTrace();
		}
		frame.setAutoRequestFocus(true);
		frame.setVisible(true);
		while(!view.hasFocus()) {
			try {
				synchronized(waiter) {
					waiter.wait(10);
				}
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testJump() {
		robot.keyPress(KeyEvent.VK_SPACE);
		if(!jump) {
			robot.keyRelease(KeyEvent.VK_SPACE);
			Assertions.fail("Did not jump!");
		}
		jump = false;
		robot.keyRelease(KeyEvent.VK_SPACE);
		if(jump) {
			Assertions.fail("Did jump!");
		}
	}

	@Test
	public void testSneak() {
		robot.keyPress(KeyEvent.VK_SHIFT);
		if(!sneak) {
			robot.keyRelease(KeyEvent.VK_SHIFT);
			Assertions.fail("Did not start to sneak!");
		}
		robot.keyRelease(KeyEvent.VK_SHIFT);
		if(sneak) {
			Assertions.fail("Did not stop to sneak!");
		}
	}
}