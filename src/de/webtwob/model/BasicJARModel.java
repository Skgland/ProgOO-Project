package de.webtwob.model;

import de.webtwob.interfaces.IJARInput;
import de.webtwob.interfaces.IJARModel;
import de.webtwob.interfaces.IJARView;
import de.webtwob.interfaces.IMenuEntry;

import java.util.ArrayList;

/**
 * Created by BB20101997 on 02. Feb. 2017.
 */
public class BasicJARModel implements IJARModel {

	private static final double SNEAK_HEIGHT  = 2;
	private static final double NORMAL_HEIGHT = 4;
	private static final double JUMP_VELOCITY = 2;

	private final IMenuEntry[] MAIN_MENU = new IMenuEntry[]{new BasicMenuEntry(() -> {//
		//TODO reset game;
		gameTime = 0;
		mode = Mode.GAME;
		synchronized(BasicJARModel.this.gameLoop) {
			BasicJARModel.this.gameLoop.notifyAll();
		}
	}, "Start", true), new BasicMenuEntry(() -> {
		stop();
		System.exit(0);
	}, "Quit", true)};

	private final IMenuEntry[] PAUSE_MENU = new IMenuEntry[]{new BasicMenuEntry(() -> {
		mode = Mode.GAME;
		synchronized(BasicJARModel.this.gameLoop) {
			BasicJARModel.this.gameLoop.notifyAll();
		}
	}, "Continue", true), new BasicMenuEntry(() -> {
		menu = 0;
		selection = 0;
	}, "Main Menu", true)};

	private final IMenuEntry[][] menues = new IMenuEntry[][]{MAIN_MENU, PAUSE_MENU};

	private          double   player_y_pos      = 0;
	private          double   player_height     = NORMAL_HEIGHT;
	private          long     gameTime          = 0;
	private          double   player_y_velocity = 0;
	private volatile int      selection         = 0;
	private volatile int      menu              = 0;
	private volatile Mode     mode              = Mode.MENU;
	private volatile boolean  running           = false;
	private final    GameLoop gameLoop          = new GameLoop();
	private Thread loop;

	private ArrayList<IJARInput> inputs = new ArrayList<>();
	private ArrayList<IJARView>  views  = new ArrayList<>();

	@Override
	public void start() {
		running = true;
		if(loop == null) {
			loop = new Thread(gameLoop);
			loop.setName("Main GameLoop");
			if(views.isEmpty()) {
				loop.setDaemon(false);
			}
			loop.start();
		}
		for(IJARInput ijarInput : inputs) {
			ijarInput.start();
		}
		for(IJARView ijarView : views) {
			ijarView.start();
		}
	}

	@Override
	public void stop() {
		running = false;
		mode = Mode.MENU;
		loop = null;
		for(IJARInput ijarInput : inputs) {
			ijarInput.stop();
		}
		for(IJARView ijarView : views) {
			ijarView.stop();
		}
	}

	@Override
	public void addView(IJARView ijarv) {
		ijarv.linkModel(this);
		views.add(ijarv);
		if(running) { ijarv.start(); }
	}

	@Override
	public void addInput(IJARInput ijari) {
		ijari.linkModel(this);
		inputs.add(ijari);
		if(running) { ijari.start(); }
	}

	@Override
	public Mode getMode() {
		return mode;
	}

	@Override
	public void jump() {
		if(player_y_pos == 0 && mode == Mode.GAME) {
			player_y_velocity = JUMP_VELOCITY;
		}
	}

	@Override
	public void setSneaking(boolean bool) {
		player_height = bool ? SNEAK_HEIGHT : NORMAL_HEIGHT;
	}

	@Override
	public boolean isSneaking() {
		return player_height == SNEAK_HEIGHT;
	}

	@Override
	public void pause() {
		if(mode == Mode.GAME) {
			mode = Mode.MENU;
			menu = 1;
		}
	}

	@Override
	public void doSelect() {
		if(mode == Mode.MENU) {
			menues[menu][selection].executeAction();
		}
	}

	@Override
	public void select(int i) {
		selection = i;
	}

	@Override
	public void up() {
		if(mode == Mode.MENU) {
			selection = (selection + 1) % menues[menu].length;
		}
	}

	@Override
	public void down() {
		if(mode == Mode.MENU) {
			selection = (selection + menues[menu].length - 1) % menues[menu].length;
		}
	}

	@Override
	public IMenuEntry[] getMenuEntries() {
		return menues[menu];
	}

	@Override
	public int getSelectedIndex() {
		return selection;
	}

	@Override
	public String toString() {
		return "[BasicJARModel]:\n" + "\t[Mode]: " + mode + "\n" + "\t[Menu]: " + menu + "\n" + "\t[Selection]: " + selection + ":" + menues[menu][selection].getText() + "\n" + "\t[Sneaking]: " + isSneaking() + "\n" + "\t[Player-Y]: " + player_y_pos + "\n" + "\t[Time]: " + gameTime + "\n";
	}

	private class GameLoop implements Runnable {

		public void run() {
			while(running) {
				while(mode == Mode.GAME) {
					gameTime++;
					if(player_y_pos != 0 || player_y_velocity > 0) {
						player_y_pos += player_y_velocity;
						player_y_velocity -= 0.5;
					}
					if(player_y_pos < 0) {
						player_y_pos = 0;
					}

					//TODO splayer should be stable and all hurdles should move towards the player

					try {
						synchronized(this) {
							this.wait(50);
						}
					}
					catch(InterruptedException e) {
						e.printStackTrace();
					}
				}
				try {
					synchronized(this) {
						this.wait();
					}
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
