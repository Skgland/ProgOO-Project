package de.webtwob.interfaces;

/**
 * Created by BB20101997 on 31. Jan. 2017.
 *
 * All methods not supported in the current mode should if not specified
 * not throw an exception but rather return a value that would not occur
 * in normal operation or is a out of bound value or a default value
 */
public interface IJARModel extends IJARRunable {

	enum Mode{
		MENU,GAME
	}

	default void addView(IJARView ijarv){
		ijarv.linkModel(this);
	}

	default void addInput(IJARInput ijari){
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
	 * @param bool true for sneaking, false for not sneaking
	 * */
	void setSneaking(boolean bool);

	/**
	 *@return if the player is sneaking, if not in GAME false
	 * */
	boolean isSneaking();

	/**
	 * causes to game to go into the pause screen
	 * */
	void pause();

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
	IMenuEntry[] getMenuEntries();

	/**
	 * @return the selected Menu Entry, default is 0
	 * */
	int getSelectedIndex();
}
