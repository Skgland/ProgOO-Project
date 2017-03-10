package de.webtwob.model;

import de.webtwob.interfaces.IJARGameModel;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;

/**
 * @author Bennet Blessmann Created on 09.03.2017.
 */
public class BasicGameModel implements IJARGameModel {

    private static final double SNEAK_HEIGHT  = 2;
    private static final double NORMAL_HEIGHT = 4;
    private static final double JUMP_VELOCITY = 2;

    private final Random      r             = new Random();
    /**
     * The Players Y Position
     */
    private       double      player_y_pos  = 0;
    /**
     * The Players Height
     */
    private       double      player_height = NORMAL_HEIGHT;
    private       Rectangle[] rects         = new Rectangle[30];
    /**
     * The Players vertical velocity
     */
    private double player_y_velocity;
    /**
     * The GameCycles since the game was started
     */
    private long gameTime    = 0;
    /**
     * All additionally score not resulting from survival time
     */
    private long bonus_score = 0;
    /**
     * the minimum cycles till the next spawn
     */
    private int  wait        = 0;

    @Override
    public boolean cycle() {

        gameTime++;
        //update player position and velocity
        if(player_y_pos != 0 || player_y_velocity > 0) {
            player_y_pos += player_y_velocity;
            player_y_velocity -= 0.5;
        }

        //stuck in floor? let's fix that!
        if(player_y_pos < 0) {
            player_y_pos = 0;
        }

        //move hurdles
        System.arraycopy(rects, 1, rects, 0, rects.length - 1);

        //if there was enough space since last hurdle generate new one
        if(wait <= 0 && r.nextDouble() < 0.7) {
            if(r.nextDouble() < 0.7) {
                //generate floor hurdle
                rects[rects.length - 1] = new Rectangle(0, 0, 1, r.nextInt(3) + 1);
            } else {
                //generate floating hurdle
                final int y = r.nextInt(2) + 2;
                rects[rects.length - 1] = new Rectangle(0, y, 1, 6 - y);
            }
            wait = 8 + r.nextInt(5);
        } else {
            //noinspection AssignmentToNull
            rects[rects.length - 1] = null;
            wait--;
        }

        if(rects[1] != null) {
            if(player_y_pos >= rects[1].getY() && rects[1].getY() + rects[1].getHeight() > player_y_pos) {
                return false;
            }

            if(player_y_pos + player_height > rects[1].getY() && rects[1].getY() + rects[1].getHeight() >
                                                                         player_y_pos + player_height) {
                //player hurt his head at a hurdle
                return false;
            }
        }
        return true;
    }

    @Override
    public void jump() {

        if(player_y_pos == 0) {
            player_y_velocity = JUMP_VELOCITY;
        }
    }

    @Override
    public boolean isSneaking() {

        return player_height == SNEAK_HEIGHT;
    }

    @Override
    public void setSneaking(final boolean sneak) {

        player_height = sneak ? SNEAK_HEIGHT : NORMAL_HEIGHT;
    }

    @Override
    public double getPlayerY() {

        return player_y_pos;
    }

    @Override
    public double getPlayerHeight() {

        return player_height;
    }

    @Override
    public Rectangle[] getHurdles() {

        return Arrays.copyOf(rects, rects.length);
    }

    @Override
    public long getTime() {

        return gameTime;
    }

    @Override
    public long getScore() {

        return gameTime / 10 + bonus_score;
    }

    @Override
    public void reset() {

        gameTime = 0;
        bonus_score = 0;
        player_y_pos = 0;
        player_y_velocity = 0;
        wait = 0;
        rects = new Rectangle[rects.length];
    }

    @Override
    public String toString() {
        return "BasicGameModel{" + "r=" + r + ", player_y_pos=" + player_y_pos + ", player_height=" + player_height +
                       ", rects=" + Arrays.toString(rects) + ", player_y_velocity=" + player_y_velocity + ", " +
                       "gameTime=" + gameTime + ", bonus_score=" + bonus_score + ", wait=" + wait + '}';
    }


}
