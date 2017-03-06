package de.webtwob.view.lighthouse;

import de.cau.infprogoo.lighthouse.LighthouseNetwork;
import de.webtwob.interfaces.IJARModel;
import de.webtwob.interfaces.IJARView;

import java.awt.*;
import java.io.IOException;
import java.util.Random;

/**
 * @author Bennet Blessmann Created on 10.02.2017.
 */
public class LightHouseView implements IJARView {

    private final String address;
    private final int    port;
    private final byte[] windows = new byte[1176];
    private LighthouseNetwork lhn;
    private Thread            updateThread;
    private boolean           run;
    private IJARModel         model;
    private final Runnable updateLoop   = this::update;
    private       Random   rng          = new Random();
    private       Color    currentColor = new Color(rng.nextInt());
    private       Color    nextColor    = new Color(rng.nextInt());
    private boolean wait;


    public LightHouseView() {

        this("localhost", 8000);
    }

    public LightHouseView(final String address, final int port) {

        if(0 > port || port > 65535) {
            throw new IllegalArgumentException("Port has to be in the Interval [0,65535] but was " + port + "!");
        }
        this.address = address;
        this.port = port;
    }

    @Override
    public void linkModel(final IJARModel ijarm) {

        model = ijarm;
    }

    @Override
    public void forceUpdate() {

        synchronized(updateLoop) {
            updateLoop.notifyAll();
        }
    }

    private boolean connect() {

        try {
            lhn = new LighthouseNetwork(address, port);
            lhn.connect();
            return true;
        }
        catch(final IOException e) {
            System.err.println("Couldn't connect to LightHouse!");
            return false;
        }
    }

    private void send(final byte[] windows) {

        assert windows.length == 1176 : "Unexpected window count: " + windows.length;
        try {
            lhn.send(windows);
        }
        catch(final IOException e) {
            System.err.println("Failed to send data to LightHouse! Dropping frame!");
        }
    }

    private void update() {

        if(connect()) {
            while(run) {
                if(model != null) {
                    try {//set background
                        final long time = (model.getTime() % 60) - 5;

                        if(time == 30) {
                            if(!wait) {
                                wait = true;
                                currentColor = nextColor;
                                nextColor = new Color(rng.nextInt());
                            }
                        } else {
                            wait = false;
                        }

                        for(byte x = 0; x < 28; x++) {
                            for(byte y = 0; y < 14; y++) {
                                if(time >= 30 || x < (28 - time)) {
                                    setWindowColor(x, y, currentColor);
                                } else {
                                    setWindowColor(x, y, nextColor);
                                }
                            }
                        }

                        final byte player_y     = (byte) model.getPlayerY();
                        final byte player_y_top = (byte) (model.getPlayerY() + model.getPlayerHeight());

                        //set floor
                        for(byte x = 0; x < 28; x++) {
                            setWindowColor(x, (byte) 12, Color.GRAY);
                        }

                        final Rectangle[] rects = model.getHurdles();
                        final byte        size  = (byte) rects.length;
                        for(byte i = 0; i < size && i < 28; i++) {
                            if(rects[i] != null) {
                                for(byte x = 0; x < rects[i].getWidth(); x++) {
                                    for(byte y = 0; y < rects[i].getHeight(); y++) {
                                        setWindowColor((byte) (i + x), (byte) (11 - rects[i].getY() - y), Color.CYAN);
                                    }
                                }
                            }
                        }

                        //paint player
                        for(byte y = player_y; y < player_y_top; y++) {
                            setWindowColor((byte) 1, (byte) (11 - y), Color.YELLOW);
                        }

                        send(windows);
                        synchronized(updateLoop) {
                            updateLoop.wait(10);
                        }
                    }
                    catch(final NullPointerException | InterruptedException ignore) {
                        //Nullpointer can occur when model is set to null by another thread after null check
                    }
                } else {
                    synchronized(updateLoop) {
                        if(model == null) {
                            try {
                                updateLoop.wait();
                            }
                            catch(final InterruptedException ignore) {
                            }
                        }
                    }
                }
            }
        } else {
            run = false;
        }
    }

    private void setWindowColor(final byte x, final byte y, final Color color) {

        assert x >= 0 && x < 28 : "Window X-Coordinate ot of bounds was:" + x;
        assert y >= 0 && y < 14 : "Window Y-Coordinate ot of bounds was:" + y;

        windows[coordToWinNumber(x, y)] = (byte) color.getRed();
        windows[coordToWinNumber(x, y) + 1] = (byte) color.getGreen();
        windows[coordToWinNumber(x, y) + 2] = (byte) color.getBlue();
    }

    private short coordToWinNumber(final byte x, final byte y) {

        assert x >= 0 && x < 28 : "Window X-Coordinate ot of bounds was:" + x;
        assert y >= 0 && y < 14 : "Window Y-Coordinate ot of bounds was:" + y;

        return (short) (x * 3 + y * 28 * 3);
    }

    @Override
    public synchronized void start() {
        if(!run) {
            run = true;
            updateThread = new Thread(updateLoop);
            updateThread.setName("LightHouseView");
            updateThread.setDaemon(true);
            updateThread.start();
        }
    }

    @Override
    public synchronized void stop() {
        if(run) {
            run = false;
            synchronized(updateLoop) {
                updateLoop.notifyAll();
            }
            while(updateThread.isAlive()) {
                try {
                    updateThread.join();
                }
                catch(final InterruptedException ignore) {
                }
            }
            updateThread = null;
        }
    }
}
