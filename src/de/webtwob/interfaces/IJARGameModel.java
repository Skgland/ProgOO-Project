package de.webtwob.interfaces;

import java.awt.*;

/**
 * @author Bennet Blessmann
 *         Created on 09.03.2017.
 */
public interface IJARGameModel {

    /**
     * executes one game cycle
     * @return true if player lives, if died false
     * */
    boolean cycle();

    /**
     *Perform a jump if possible
     * */
    void jump();

    /**
     * Set if the Character is sneaking
     * @param sneak true for sneaking, false for not sneaking
     * */
    void setSneaking(boolean sneak);

    /**
     *@return if the player is sneaking, always false if not in game
     * */
    boolean isSneaking();

    /**
     * @return the players y coordinate
     * */
    double getPlayerY();

    /**
     * @return the players height
     * */
    double getPlayerHeight();

    /**
     * @return the array of hurdles currently in the game
     * the place in the array corresponds to it's position
     * 0 being 1 unit behind the player,
     * 1 being at the players position etc.
     * */
    Rectangle[] getHurdles();

    /**
     * @return the current playtime, may be an arbitrary unit of measurement
     */
    long getTime();

    /**
     * @return the current score
     * if score is likely to be lost again this may be excluded
     * and only be included if certain that it will be kept
     */
    long getScore();

    /**
     * Resets the game
     * */
    void reset();
}
