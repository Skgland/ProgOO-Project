package main;

import de.webtwob.input.auto.AutoRun;
import de.webtwob.input.controller.ControllerInput;
import de.webtwob.input.gameloop.GameLoop;
import de.webtwob.input.keyboard.KeyboardInput;
import de.webtwob.interfaces.IJARGameModel;
import de.webtwob.interfaces.IJARInput;
import de.webtwob.interfaces.IJARMenuModel;
import de.webtwob.interfaces.IJARView;
import de.webtwob.model.BasicGameModel;
import de.webtwob.model.BasicMenuModel;
import de.webtwob.model.ModeModel;
import de.webtwob.view.lighthouse.LightHouseView;
import de.webtwob.view.swing.BasicView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author Bennet Blessmann Created on 10.02.2017.
 */
public class Main {

    /**
     * initializes the Model, all Views and Inputs
     */
    public static void main(final String[] tArgs) {
        final ArrayList<IJARInput> inputs = new ArrayList<>();

        final ModeModel     mode = new ModeModel();
        final IJARGameModel game = new BasicGameModel();
        final IJARMenuModel menu = new BasicMenuModel(game, mode);

        final IJARView  view   = new BasicView(game, menu, mode);
        final IJARView  viewLH = new LightHouseView(game, menu, mode);
        final IJARInput loop   = new GameLoop(mode, game, menu);
        final IJARInput auto   = new AutoRun(game);
        final IJARInput controller;

        {
            IJARInput in;

            try {
                in = new ControllerInput(mode, game, menu);
            }
            catch(final InternalError ignore) {
                //noinspection AssignmentToNull
                in = null;
                System.err.println("Failed to Initialize GLFW! If running on Mac try starting JVM with " +
                                           "-XstartOnFirstThread!");
            }
            catch(final UnsatisfiedLinkError | NoClassDefFoundError e) {
                //noinspection AssignmentToNull
                in = null;
                System.err.println("Couldn't find Controller libraries!\nController support disabled!");
            }

            controller = in;
        }

        //setup the JFrame
        final JFrame jFrame = new JFrame();
        jFrame.add((Component) view);

        jFrame.setMinimumSize(new Dimension(400, 200));
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setAutoRequestFocus(true);
        jFrame.setVisible(true);

        final IJARInput keyboard = new KeyboardInput(jFrame.getRootPane(), game, menu, mode);

        //add all inputs and views to the model
        //the model will handel linking the views and inputs to itself
        inputs.add(loop);
        inputs.add(auto);
        inputs.add(keyboard);

        if(controller != null) {
            inputs.add(controller);
            controller.start();
        }

        menu.setInputList(inputs);

        view.start();
        viewLH.start();
        keyboard.start();
        loop.start();
        auto.start();

        /*
         * The GLFWQueue has to be run by the main Thread,
         * therefor our last Task is to run it
         * */
        GLFWQueue.getInstance().run();
    }

}
