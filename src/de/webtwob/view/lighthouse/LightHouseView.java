package de.webtwob.view.lighthouse;

import de.cau.infprogoo.lighthouse.LighthouseNetwork;
import de.webtwob.interfaces.IJARModel;
import de.webtwob.interfaces.IJARView;

import java.awt.*;
import java.io.IOException;

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
    private final Runnable updateLoop = this::update;

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

    int i = 0;
    int n = 0;

    private void update() {

        if(connect()) {
            while(run) {
                if(model != null) {
                    try {

                        if(model.getMode() == IJARModel.Mode.MENU) {
                            //space for two lines of max. 7 letters
                            for(byte x = 0; x < 28; x++) {
                                for(byte y = 0; y < 14; y++) {
                                    setWindowColor(x, y, Color.BLUE);
                                }
                            }

                            switch(model.getCurrentMenu().getText()) {
                                case "Game Over": {
                                    long score = model.getScore();

                                    if(score < 100_000) {

                                        byte xoff = 4;
                                        for(byte i = 0; i < Letter.SCORE.length; i++) {
                                            setWindowPattern(xoff, (byte) 1, Letter.SCORE[i], Color.RED);
                                            xoff += Letter.SCORE[i][0].length + 1;
                                        }

                                        int index = 0;
                                        int n;

                                        while(score > 0) {
                                            n = (int) (score % 10);
                                            setWindowPattern((byte) (20 - 4 * index), (byte) 7, Letter.NUMBERS[n],
                                                             Color.RED);
                                            index++;
                                            score /= 10;
                                        }

                                    } else {
                                        //score to high
                                        byte xoff  = 5;
                                        byte xoff2 = 6;
                                        for(int i = 0; i < 4; i++) {
                                            setWindowPattern(xoff, (byte) 1, Letter.GAME[i], Color.RED);
                                            setWindowPattern(xoff2, (byte) 7, Letter.OVER[i], Color.RED);
                                            xoff += Letter.GAME[i][0].length + 1;
                                            xoff2 += Letter.OVER[i][0].length + 1;
                                        }
                                    }
                                    break;
                                }
                                case "Main Menu": {
                                    byte xoff = 4;
                                    for(byte i = 0; i < Letter.MAIN.length; i++) {
                                        setWindowPattern(xoff, (byte) 1, Letter.MAIN[i], Color.RED);
                                        xoff += Letter.MAIN[i][0].length + 1;
                                    }

                                    xoff = 4;
                                    for(byte i = 0; i < Letter.MENU.length; i++) {
                                        setWindowPattern(xoff, (byte) 7, Letter.MENU[i], Color.RED);
                                        xoff += Letter.MENU[i][0].length + 1;
                                    }
                                    break;
                                }
                                default: {
                                    byte xoff = 4;
                                    for(byte i = 0; i < Letter.MENU.length; i++) {
                                        setWindowPattern(xoff, (byte) 1, Letter.MENU[i], Color.RED);
                                        xoff += Letter.MENU[i][0].length + 1;
                                    }
                                }
                            }
                            send(windows);
                            synchronized(updateLoop) {
                                updateLoop.wait(10);
                            }
                        } else {
                            //set background
                            for(byte x = 0; x < 28; x++) {
                                for(byte y = 0; y < 14; y++) {
                                    setWindowColor(x, y, Color.BLUE);
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
        final int index = coordToWinNumber(x, y);

        if(index + 2 >= windows.length) {
            return;
        }

        windows[index] = (byte) color.getRed();
        windows[index + 1] = (byte) color.getGreen();
        windows[index + 2] = (byte) color.getBlue();
    }

    /**
     * @param x       x-offset
     * @param y       y-offset
     * @param pattern not as may be intuitive [x][y] instead [y][x]
     */
    private void setWindowPattern(final byte x, final byte y, final boolean[][] pattern, Color c) {
        for(byte iy = 0; iy < pattern.length; iy++) {
            for(byte ix = 0; ix < pattern[iy].length; ix++) {
                if(pattern[iy][ix]) {
                    setWindowColor((byte) (x + ix), (byte) (y + iy), c);
                }
            }
        }
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
