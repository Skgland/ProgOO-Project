package de.webtwob;

import de.webtwob.interfaces.IJARView;

import javax.swing.*;
import java.awt.*;

/**
 * @author Bennet Blessmann
 * Created on 02. Feb. 2017.
 */
public class TestView extends JPanel implements IJARView {

    private Thread userThread;
    private volatile boolean   running = true;
    private final    JTextArea label   = new JTextArea("Running Test!");
    private IJARModel model;

    private void keepRunning() {

        while (running) {
            try {
                if (model != null) {
                    //noinspection HardcodedLineSeparator
                    label.setText("Running Test!:\n" + model.toString());
                    label.setMinimumSize(label.getPreferredSize());
                }
                synchronized (this) {
                    try {
                        wait(10);
                    } catch (final InterruptedException ignore) {
                    }
                }
            } catch (final Exception e) {
                e.printStackTrace();
                label.setText("Error!");
            }
        }
    }

    public TestView() {

        add(label);
        label.setEditable(false);
        label.setFocusable(false);
        setMinimumSize(new Dimension(400, 200));
        setVisible(true);
    }

    @Override
    public void linkModel(final IJARModel ijarm) {

        model = ijarm;
    }

    @Override
    public void start() {

        running = true;
        if (userThread == null) {
            userThread = new Thread(this::keepRunning);
            userThread.setName("TestView Thread");
            userThread.start();
        }
    }

    @Override
    public void stop() {

        running = false;
        synchronized (this) {
            notifyAll();
        }
        userThread = null;
    }
    @Override
    public void forceUpdate() {

    }
}