package main;

import de.webtwob.input.auto.AutoRun;
import de.webtwob.input.controller.ControllerInput;
import de.webtwob.input.keyboard.KeyboardInput;
import de.webtwob.interfaces.IJARInput;
import de.webtwob.interfaces.IJARModel;
import de.webtwob.interfaces.IJARView;
import de.webtwob.model.BasicJARModel;
import de.webtwob.view.swing.BasicView;
import de.webtwob.view.lighthouse.LightHouseView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author Bennet Blessmann
 *         Created on 10.02.2017.
 */
public class Main {

    /**
     * initializes the Model, all Views and Inputs
     * */
    public static void main(final String[] tArgs){

        final IJARModel model = new BasicJARModel();
        final IJARView  view  = new BasicView();
        final IJARView  viewLH = new LightHouseView();
        final IJARInput auto = new AutoRun();
        final IJARInput in2;
        IJARInput       in;

        try {
              in = new ControllerInput();
        } catch(final InternalError ignore){
            in = null;
        }catch (final UnsatisfiedLinkError e) {
            //in case the jinput.jar is not in the class path
            //noinspection AssignmentToNull
            in = null;
            System.err.println("Couldn't find Controller libraries!\nController support disabled!");
        }

        in2 = in;

        //setup the JFrame
        final JFrame jFrame = new JFrame();
        JMenuBar jmb = new JMenuBar();
        jmb.add(new JMenuItem(new AbstractAction("Exit") {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        }));
        jFrame.setJMenuBar(jmb);
        jFrame.add((Component) view);

        jFrame.setMinimumSize(new Dimension(400,200));
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setAutoRequestFocus(true);
        jFrame.setVisible(true);

        final IJARInput input = new KeyboardInput(jFrame.getRootPane());

        //add all to the model
        model.addView(view);
        model.addView(viewLH);
        model.addInput(input);
        model.addInput(auto);

        if(in2!=null) {
            model.addInput(in2);
        }

        model.start();
    }

}
