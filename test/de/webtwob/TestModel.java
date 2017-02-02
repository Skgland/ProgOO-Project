package de.webtwob;

import de.webtwob.interfaces.IJARInput;
import de.webtwob.interfaces.IJARModel;
import de.webtwob.interfaces.IJARView;
import de.webtwob.interfaces.IMenuEntry;

import java.util.ArrayList;

/**
 * Created by BB20101997 on 02. Feb. 2017.
 */
public class TestModel implements IJARModel {

	public boolean jump;
	public boolean sneak;
	public Mode mode = Mode.GAME;

	private ArrayList<IJARInput> inputs = new ArrayList<>();
	private ArrayList<IJARView> views = new ArrayList<>();

	@Override
	public void addView(IJARView ijarv) {
		ijarv.linkModel(this);
			views.add(ijarv);
	}

	@Override
	public void addInput(IJARInput ijari) {
		ijari.linkModel(this);
		inputs.add(ijari);
	}

	@Override
	public IJARModel.Mode getMode() {
		return mode;
	}

	@Override
	public void jump() {
		if(mode == Mode.GAME) {
			System.out.println("Jump");
			jump = true;
		}
	}

	@Override
	public void setSneaking(boolean bool) {
		sneak = bool;
		if(mode == Mode.GAME) {
			System.out.println(bool ? "Sneaking" : "Not Sneaking");
		}
	}

	@Override
	public boolean isSneaking() {
		return sneak && mode == Mode.GAME;
	}

	@Override
	public void pause() {
		if(mode == Mode.GAME) {
			mode = Mode.MENU;
			System.out.println("Pause");
		} else {
			mode = Mode.GAME;
			System.out.println("Unpause");
		}
	}

	@Override
	public void doSelect() {
		if(mode == Mode.MENU) {
			System.out.println("Selected");
		}
	}

	@Override
	public void select(int i) {

	}

	@Override
	public void up() {
		if(mode == Mode.MENU) {
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
	public IMenuEntry[] getMenuEntries() {
		return new IMenuEntry[0];
	}

	@Override
	public int getSelectedIndex() {
		return 0;
	}

	@Override
	public void start() {
		for(IJARView ijarView : views) {
			ijarView.start();
		}
		for(IJARInput ijarInput : inputs) {
			ijarInput.start();
		}
	}

	@Override
	public void stop() {
		for(IJARInput ijarInput : inputs) {
			ijarInput.stop();
		}
		for(IJARView ijarView : views) {
			ijarView.stop();
		}
	}

	@Override
	public String toString() {
		return "[TestModel]:\n" +
				       "\t[Sneaking]: "+sneak+"\n";
	}
}