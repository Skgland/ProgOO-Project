package de.webtwob.view.lighthouse;

import de.cau.infprogoo.lighthouse.LighthouseNetwork;
import de.webtwob.interfaces.IJARGameModel;
import de.webtwob.interfaces.IJARMenuModel;
import de.webtwob.interfaces.IJARView;
import de.webtwob.interfaces.Mode;
import de.webtwob.model.menu.ModeModel;

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
    int i = 0;
    int n = 0;
    private LighthouseNetwork lhn;
    private Thread            updateThread;
    private boolean           run;
    private IJARMenuModel     menu;
    private IJARGameModel     game;
    private ModeModel         mode;
    private Random rng          = new Random();
    private Color  currentColor = new Color(rng.nextInt());
    private Color  nextColor    = new Color(rng.nextInt());
    private boolean wait;
    private final Runnable updateLoop = this::update;

    public LightHouseView(IJARGameModel game, IJARMenuModel menu, ModeModel mode) {

        this(game, menu, mode, "localhost", 8000);
    }

    public LightHouseView(IJARGameModel game, IJARMenuModel menu, ModeModel mode, final String address, final int port) {

        if (0 > port || port > 65535) {
            throw new IllegalArgumentException("Port has to be in the Interval [0,65535] but was " + port + "!");
        }
        this.menu = menu;
        this.game = game;
        this.mode = mode;
        this.address = address;
        this.port = port;
    }

    @Override
    public void forceUpdate() {

        synchronized (updateLoop) {
            updateLoop.notifyAll();
        }
    }
    private boolean connect() {

        try {
            lhn = new LighthouseNetwork(address, port);
            lhn.connect();
            return true;
        } catch (final IOException e) {
            System.err.println("Couldn't connect to LightHouse!");
            return false;
        }
    }
    private void send(final byte[] windows) {

        assert windows.length == 1176 : "Unexpected window count: " + windows.length;
        try {
            lhn.send(windows);
        } catch (final IOException e) {
            System.err.println("Failed to send data to LightHouse! Dropping frame!");
        }
    }
    private void update() {

        if (connect()) {
            while (run) {
                try {
                    if (mode.getMode() == Mode.MENU) {
                        //space for two lines of max. 7 letters
                        for (byte x = 0; x < 28; x++) {
                            for (byte y = 0; y < 14; y++) {
                                setWindowColor(x, y, Color.BLUE);
                            }
                        }

                        switch (menu.getCurrentMenu().getText()) {
                            case "Game Over": {
                                long score = game.getScore();

                                if (score < 100_000) {

                                    setPatterns(4, 1, 1, Letter.SCORE, Color.RED);

                                    int index = 0;
                                    int n;

                                    while (score > 0) {
                                        n = (int) (score % 10);
                                        setWindowPattern((byte) (20 - 4 * index), (byte) 7, Letter.NUMBERS[n],
                                                         Color.RED
                                        );
                                        index++;
                                        score /= 10;
                                    }

                                } else {
                                    //score to high
                                    setPatterns(5, 1, 1, Letter.GAME, Color.RED);
                                    setPatterns(6, 7, 1, Letter.OVER, Color.RED);
                                }
                                break;
                            }
                            case "Main Menu": {
                                setPatterns(4, 1, 1, Letter.MAIN, Color.RED);
                                setPatterns(4, 7, 1, Letter.MENU, Color.RED);
                                break;
                            }
                            default: {
                                setPatterns(4, 1, 1, Letter.MENU, Color.RED);
                            }
                        }
                        send(windows);
                        synchronized (updateLoop) {
                            updateLoop.wait(10);
                        }
                    } else {
                        //set background
                        final long time = (game.getTime() % 60) - 5;

                        if (time == 30) {
                            if (!wait) {
                                wait = true;
                                currentColor = nextColor;
                                do {
                                    nextColor = new Color(rng.nextInt());
                                } while (colorDiff(Color.CYAN, nextColor) < 50 || colorDiff(
                                        Color.YELLOW,
                                        nextColor
                                ) < 50);
                            }
                        } else {
                            wait = false;
                        }

                        for (byte x = 0; x < 28; x++) {
                            for (byte y = 0; y < 14; y++) {
                                if (time >= 30 || x < (28 - time)) {
                                    setWindowColor(x, y, currentColor);
                                } else {
                                    setWindowColor(x, y, nextColor);
                                }
                            }
                        }

                        final byte player_y     = (byte) game.getPlayerY();
                        final byte player_y_top = (byte) (game.getPlayerY() + game.getPlayerHeight());

                        //set floor
                        for (byte x = 0; x < 28; x++) {
                            setWindowColor(x, (byte) 12, Color.GRAY);
                        }

                        final Rectangle[] rects = game.getHurdles();
                        final byte        size  = (byte) rects.length;
                        for (byte b = 0; b < size && b < 28; b++) {
                            if (rects[b] != null) {
                                for (byte x = 0; x < rects[b].getWidth(); x++) {
                                    for (byte y = 0; y < rects[b].getHeight(); y++) {
                                        setWindowColor(
                                                (byte) (b + x),
                                                (byte) (11 - rects[b].getY() - y),
                                                Color.CYAN
                                        );
                                    }
                                }
                            }
                        }

                        //paint player
                        for (byte y = player_y; y < player_y_top; y++) {
                            setWindowColor((byte) 1, (byte) (11 - y), Color.YELLOW);
                        }

                        send(windows);
                        synchronized (updateLoop) {
                            updateLoop.wait(10);
                        }
                    }
                } catch (final NullPointerException | InterruptedException ignore) {
                    //Nullpointer can occur when model is set to null by another thread after null check
                }
            }
        } else {
            run = false;
        }
    }

    private void setWindowColor(final byte x, final byte y, final Color color) {

        final int index = coordToWinNumber(x, y);

        if (index + 2 >= windows.length) {
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
    private void setWindowPattern(final byte x, final byte y, final boolean[][] pattern, final Color c) {

        for (byte iy = 0; iy < pattern.length; iy++) {
            for (byte ix = 0; ix < pattern[iy].length; ix++) {
                if (pattern[iy][ix]) {
                    setWindowColor((byte) (x + ix), (byte) (y + iy), c);
                }
            }
        }
    }

    private int colorDiff(final Color a, final Color b) {

        final int green = Math.abs(a.getGreen() - b.getGreen());
        final int red   = Math.abs(a.getRed() - b.getRed());
        final int blue  = Math.abs(a.getBlue() - b.getBlue());
        return green + blue + red;
    }

    /**
     * annoyed of typecasting constant parameters
     */
    private void setPatterns(final int x, final int y, final int offset, final boolean[][][] over, final Color red) {

        setPatterns((byte) x, (byte) y, (byte) offset, over, red);
    }

    /**
     * @param x        the x-Coordinat to start at
     * @param y        the y-Coordinate to start at
     * @param offset   the distance between each pattern
     * @param patterns the array of patterns to draw
     * @param c        The Color to set the Patterns to
     */
    private void setPatterns(final byte x, final byte y, final byte offset, final boolean[][][] patterns, final Color
            c) {

        byte xOffset = x;
        for (final boolean[][] pattern : patterns) {
            setWindowPattern(xOffset, y, pattern, c);
            xOffset += pattern[0].length + offset;
        }
    }

    private short coordToWinNumber(final byte x, final byte y) {

        assert x >= 0 && x < 28 : "Window X-Coordinate ot of bounds was:" + x;
        assert y >= 0 && y < 14 : "Window Y-Coordinate ot of bounds was:" + y;

        return (short) (x * 3 + y * 28 * 3);
    }

    @Override
    public synchronized void start() {

        if (!run) {
            run = true;
            updateThread = new Thread(updateLoop);
            updateThread.setName("LightHouseView");
            updateThread.setDaemon(true);
            updateThread.start();
        }
    }

    @Override
    public synchronized void stop() {

        if (run) {
            run = false;
            synchronized (updateLoop) {
                updateLoop.notifyAll();
            }
            while (updateThread.isAlive()) {
                try {
                    updateThread.join();
                } catch (final InterruptedException ignore) {
                }
            }
            updateThread = null;
        }
    }
}
