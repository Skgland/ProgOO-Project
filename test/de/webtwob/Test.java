package de.webtwob;

import de.webtwob.input.controller.ControllerInput;
import de.webtwob.input.keyboard.KeyboardInput;
import de.webtwob.interfaces.IJARInput;
import de.webtwob.interfaces.IJARModel;
import de.webtwob.interfaces.IJARView;
import de.webtwob.model.BasicJARModel;
import de.webtwob.view.BasicView;

import javax.swing.*;
import java.awt.*;

/**
 * Created by BB20101997 on 01. Feb. 2017.
 */
public class Test {

	private static  IJARModel model;
	private static  IJARView  view;
	private static  IJARInput input;

	public static void main(String[] tArgs) {
		IJARInput in2;

		JFrame jFrame = new JFrame();
		jFrame.setMinimumSize(new Dimension(400,200));
		jFrame.pack();
		jFrame.setAutoRequestFocus(true);
		jFrame.setVisible(true);
		jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		model = new BasicJARModel();
		view = new BasicView();
		jFrame.add((Component) view);
		input = new KeyboardInput(jFrame.getRootPane());

		model.addView(view);
		model.addInput(input);

		in2 = new ControllerInput();
		model.addInput(in2);

		model.start();
	}

}
