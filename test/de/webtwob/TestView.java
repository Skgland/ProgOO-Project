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


    private void keepRunning() {

        while (running) {
            try {
                label.setText("Running Test!:");
                    label.setMinimumSize(label.getPreferredSize());
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
    public String toString() {
        return "TestView{" + "userThread=" + userThread + ", running=" + running + ", label=" + label + '}';
    }
}