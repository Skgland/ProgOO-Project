package de.webtwob.interfaces;

import java.awt.*;
import java.util.List;

/**
 * Created by BB20101997 on 31. Jan. 2017.
 *
 * All methods not supported in the current mode should if not specified
 * not throw an exception but rather return a value that would not occur
 * in normal operation or is a out of bound value or a default value
 */
public interface IJARModel extends IJARRunable {

    /**
     * @return the current playtime, may be an arbitrary unit of messurement
     * */
    long getTime();

    /**
     * @return the current score
     * if score is likely to be lost again this may be excluded
     * and only be included if certain that it will be kept
     * */
    long getScore();

    /**
     * This enum contains the possible stages of the game
     * */
    enum Mode{
        MENU,GAME
    }

    /**
     * by default this will link the provided View to the model
     * implementations may start the view as well
     * */
    default void addView(final IJARView ijarv){
        ijarv.linkModel(this);
    }


    /**
     * by default this will link the provided Input to the model
     * implementations may start the input as well
     * */
    default void addInput(final IJARInput ijari){
        ijari.linkModel(this);
    }

    /**
     * @return the current mode of operation
     * */
    Mode getMode();

    /*===========================================================*
    * ===================GAME MODE ONLY==========================*
    * ===========================================================*/

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
     * causes the game to go into the pause screen
     * */
    void pause();

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

    /*===========================================================*
    * ===================MENU MODE ONLY==========================*
    * ===========================================================*/

    /**
     * will cause the action associated with the current menu entry to be executed
     * */
    void doSelect();

    /**
     * Selects the Menu Item with the given indicator
     * does not execute it's associated action
     * @param i the chosen indicator
     * */
    void select(int i);

    /**
     * When in menu move selection up one
     * it's up to the model to decide if when at the top of the menu it will stay there or
     * if up on the top entry will wrap around to the bottom
     * */
    void up();

    /**
     * When in menu move selection down one
     */
    void down();

    /**+
     * @return returns menu entries when in menu mode else an empty array
     * */
    List<IMenuEntry> getMenuEntries();

    IMenu getCurrentMenu();

    /**
     * @return the selected Menu Entry, default is 0
     * */
    int getSelectedIndex();

    /**
     * if information has updated and the views
     * should to update all components this will
     * inform all views to update the ui
     * */
    void updateViews();
}
