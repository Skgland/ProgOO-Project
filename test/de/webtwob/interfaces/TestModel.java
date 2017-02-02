package de.webtwob.interfaces;

/**
 * Created by BB20101997 on 02. Feb. 2017.
 */
public class TestModel implements IJARModel {

	public boolean jump;
	public boolean sneak;
	public Mode mode = Mode.GAME;

	@Override
	public void addView(IJARView ijarv) {

	}

	@Override
	public void addInput(IJARInput ijari) {

	}

	@Override
	public IJARModel.Mode getMode() {
		return mode;
	}

	@Override
	public void jump() {
		if(mode==Mode.GAME){
			System.out.println("Jump");
			jump = true;
		}
	}

	@Override
	public void setSneaking(boolean bool) {
		sneak = bool;
		System.out.println(bool?"Sneaking":"Not Sneaking");
	}

	@Override
	public boolean isSneaking() {
		return sneak;
	}

	@Override
	public void pause() {
		if(mode==Mode.GAME){
			mode = Mode.MENU;
			System.out.println("Pause");
		}else{
			mode = Mode.GAME;
			System.out.println("Unpause");
		}
	}

	@Override
	public void doSelect() {
		if(mode==Mode.MENU) {
			System.out.println("Selected");
		}
	}

	@Override
	public void select(int i) {

	}

	@Override
	public void up() {
		if(mode==Mode.MENU){
			System.out.println("UP");
		}
	}

	@Override
	public void down() {
		if(mode == Mode.MENU) {
			System.out.println("DOWN");
		}

	}

	@Override
	public String[] getMenuEntries() {
		return new String[0];
	}

	@Override
	public int getSelectedEntry() {
		return 0;
	}

	@Override
	public void start() {

	}

	@Override
	public void stop() {

	}
}