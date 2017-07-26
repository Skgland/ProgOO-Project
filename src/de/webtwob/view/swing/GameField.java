package de.webtwob.view.swing;

import de.webtwob.interfaces.IJARGameModel;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Random;

/**
 * @author Bennet Blessmann Created on 05.02.2017.
 *         <p>
 *         This class displays the game in the BasicView
 */
public class GameField extends Canvas implements Runnable {

    private       BufferStrategy bs;
    private final IJARGameModel  game;
    private final Random rng          = new Random();
    private       Color  currentColor = new Color(rng.nextInt());
    private       Color  nextColor    = new Color(rng.nextInt());
    private boolean wait;

    public GameField(final IJARGameModel game) {

        this.game = game;
        setBackground(Color.BLUE);
        setIgnoreRepaint(true);
    }

    @Override
    public void addNotify() {

        super.addNotify();
        createBufferStrategy(2);
        bs = getBufferStrategy();
    }

    @Override
    public void removeNotify() {

        super.removeNotify();
        bs.dispose();
        bs = null;
    }

    @Override
    public String toString() {

        return "A GameField Object";
    }

    @Override
    public void run() {

        if(bs != null) {
            //Preparation work
            final double win_width  = getWidth() / 28;
            final double win_height = getHeight() / 14;

            final double player_y      = game.getPlayerY();
            final double player_height = game.getPlayerHeight();

            final double win_player_height = win_height * player_height;
            final double win_player_y      = win_height * player_y;

            final long time = (game.getTime() % 60) - 5;

            final Rectangle[] hurdles = game.getHurdles();

            //get Graphics every cycle to ensure validity
            final Graphics graphics = bs.getDrawGraphics();

            if(time == 30) {
                if(!wait) {
                    wait = true;
                    currentColor = nextColor;
                    do {
                        nextColor = new Color(rng.nextInt());
                    } while(colorDiff(Color.CYAN, nextColor) < 50 || colorDiff(Color.YELLOW, nextColor) < 50);

                }
            } else {
                wait = false;
            }

            //drawBackground
            graphics.setColor(currentColor);
            graphics.fillRect(0, 0, getWidth(), getHeight());

            if(time < 30) {
                graphics.setColor(nextColor);
                graphics.fillRect((int) (getWidth() - time * win_width), 0, (int) (time * win_width), getHeight());
            }

            //drawFlor
            graphics.setColor(Color.GRAY);
            graphics.fillRect(0, getHeight() - (int) (2 * win_height), getWidth(), (int) win_height);

            graphics.setColor(Color.CYAN);
            Rectangle rect;
            for(int i = 0; i < hurdles.length; i++) {
                rect = hurdles[i];
                if(rect != null) {
                    graphics.fillRect((int) ((i - 1) * win_width), (int) (getHeight() - ((rect.getY() + rect.getHeight() + 2) * win_height)), (int) (rect.getWidth() * win_width), (int) (rect.getHeight() * win_height));
                }
            }

            //drawPlayer
            graphics.setColor(Color.YELLOW);
            graphics.fillRect((int) win_width, getHeight() - ((int) (2 * win_height) + (int) (win_player_height) +
                                                                      (int) (win_player_y)), (int) win_width, (int) (win_player_height));

            graphics.setColor(Color.RED);
            graphics.drawString("Time: " + game.getTime(), 20, 20);
            graphics.drawString("Score: " + game.getScore(), 20, 40);

            if(!bs.contentsLost()) {
                //display buffer
                bs.show();
            }
            Toolkit.getDefaultToolkit().sync();
            //dispose of graphics
            graphics.dispose();
        }

    }

    private int colorDiff(final Color a, final Color b) {

        final int green = Math.abs(a.getGreen() - b.getGreen());
        final int red   = Math.abs(a.getRed() - b.getRed());
        final int blue  = Math.abs(a.getBlue() - b.getBlue());
        return green + blue + red;
    }
}
