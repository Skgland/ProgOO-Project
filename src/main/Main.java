package main;

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
 * @author Bennet Blessmann
 *         Created on 10.02.2017.
 */
public class Main {

	/**
	 * initializes the Model and all Views and Inputs
	 * */
	public static void main(final String[] tArgs){

		final IJARModel model = new BasicJARModel();
		final IJARView  view  = new BasicView();
		final IJARInput in2;
		IJARInput       in;

		try {
			Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass("net.java.games.input.ControllerEnvironment");
			in = new ControllerInput();
		} catch (final ClassNotFoundException e) {
			//in case the jinput.jar is not in the class path
			in = null;
		}

		in2 = in;

		final JFrame jFrame = new JFrame();
		//setup the JFrame
		jFrame.setMinimumSize(new Dimension(400,200));
		jFrame.pack();
		jFrame.setAutoRequestFocus(true);
		jFrame.setVisible(true);
		jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		jFrame.add((Component) view);

		final IJARInput input = new KeyboardInput(jFrame.getRootPane());

		//add all to the model
		model.addView(view);
		model.addInput(input);

		if(in2!=null) {
			model.addInput(in2);
		}

		model.start();
	}

}
