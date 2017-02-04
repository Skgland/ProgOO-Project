package de.webtwob.model;

import de.webtwob.interfaces.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by BB20101997 on 02. Feb. 2017.
 */
public class BasicJARModel implements IJARModel {

	private static final double SNEAK_HEIGHT  = 2;
	private static final double NORMAL_HEIGHT = 4;
	private static final double JUMP_VELOCITY = 2;

	private volatile IMenu menu;
	private          double   player_y_pos      = 0;
	private          double   player_height     = NORMAL_HEIGHT;
	private          long     gameTime          = 0;
	private          double   player_y_velocity = 0;
	private volatile int      selection         = 0;
	private volatile Mode     mode              = Mode.MENU;
	private volatile boolean  running           = false;
	private final    GameLoop gameLoop          = new GameLoop();
	private Thread loop;

	private final LinkedList<IMenu> back = new LinkedList<>();

	//TODO? add continue to main menu
	private final IMenu                MAIN_MENU     = new BasicMenu("Main Menu");
	private final IMenu                PAUSE_MENU    = new BasicMenu("Pause Menu");
	private final IMenu                SETTINGS_MENU = new BasicMenu("Settings");
	private final ArrayList<IJARInput> inputs        = new ArrayList<>();
	private final ArrayList<IJARView>  views         = new ArrayList<>();

	private final InputMenu INPUTS_MENU = new InputMenu(this);

	private final IMenuEntry START = new BasicMenuEntry(
			() -> {
				//TODO reset game;
				gameTime = 0;
				mode = Mode.GAME;
				synchronized (BasicJARModel.this.gameLoop) {
					BasicJARModel.this.gameLoop.notifyAll();
				}
			}, "Start");

	private final IMenuEntry QUIT = new BasicMenuEntry(
			() -> {
				stop();
				System.exit(0);
			}, "Quit"
	);

	private final IMenuEntry BACK = new BasicMenuEntry(
			() -> {
				menu = back.pop();
				selection = 0;
				updateViews();
			},
			"Back"
	);

	private final IMenuEntry CONTINUE = new BasicMenuEntry(
			() -> {
				mode = Mode.GAME;
				synchronized (BasicJARModel.this.gameLoop) {
					BasicJARModel.this.gameLoop.notifyAll();
				}
			}, "Continue"
	);

	private final IMenuEntry GOTO_MAIN = new BasicMenuEntry(
			() -> {
				menu = MAIN_MENU;
				selection = 0;
				updateViews();
			}, "Main Menu"
	);

	{
		MAIN_MENU.add(START);
		MAIN_MENU.add(SETTINGS_MENU);
		MAIN_MENU.add(QUIT);

		PAUSE_MENU.add(CONTINUE);
		PAUSE_MENU.add(SETTINGS_MENU);
		PAUSE_MENU.add(GOTO_MAIN);

		SETTINGS_MENU.add(INPUTS_MENU);
		SETTINGS_MENU.add(BACK);

		INPUTS_MENU.add(BACK);
		INPUTS_MENU.setAction(
				() -> {
					back.addFirst(menu);
					selection = 0;
					updateViews();
					menu = INPUTS_MENU;
				}
		);

		SETTINGS_MENU.setAction(
				() -> {
					back.addFirst(menu);
					selection = 0;
					updateViews();
					menu = SETTINGS_MENU;
				}
		);

		menu = MAIN_MENU;
	}

	@Override
	public void start() {

		running = true;
		if (loop == null) {
			loop = new Thread(gameLoop);
			loop.setName("Main GameLoop");
			if (views.isEmpty()) {
				loop.setDaemon(false);
			}
			loop.start();
		}
		for (IJARInput ijarInput : inputs) {
			ijarInput.start();
		}
		for (IJARView ijarView : views) {
			ijarView.start();
		}
	}

	@Override
	public void stop() {

		running = false;
		mode = Mode.MENU;
		loop = null;
		for (IJARInput ijarInput : inputs) {
			ijarInput.stop();
		}
		for (IJARView ijarView : views) {
			ijarView.stop();
		}
	}

	@Override
	public void addView(IJARView ijarv) {

		ijarv.linkModel(this);
		views.add(ijarv);
		if (running) {
			ijarv.start();
		}
	}

	@Override
	public void addInput(IJARInput ijari) {

		ijari.linkModel(this);
		inputs.add(ijari);
		if (running) {
			ijari.start();
		}
		INPUTS_MENU.updateInputs(inputs);
	}

	public void removeInput(IJARInput ijari) {

		if (inputs.remove(ijari)) {
			ijari.stop();
		}
		INPUTS_MENU.updateInputs(inputs);
	}

	@Override
	public Mode getMode() {

		return mode;
	}

	@Override
	public void jump() {

		if (player_y_pos == 0 && mode == Mode.GAME) {
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

		if (mode == Mode.GAME) {
			mode = Mode.MENU;
			menu = PAUSE_MENU;
		}
	}

	@Override
	public void doSelect() {

		if (mode == Mode.MENU && selection >= 0 && selection < menu.size()) {
			menu.get(selection).executeAction();
		}
	}

	@Override
	public void select(int i) {

		selection = i;
	}

	@Override
	public void up() {

		if (mode == Mode.MENU) {
			selection = (selection + menu.size() - 1) % menu.size();
		}
	}

	@Override
	public void down() {

		if (mode == Mode.MENU) {
			selection = (selection + 1) % menu.size();
		}

	}


	@Override
	public List<IMenuEntry> getMenuEntries() {

		return new ArrayList<>(menu.getEntries());
	}

	@Override
	public int getSelectedIndex() {

		return selection;
	}
	@Override
	public void updateViews() {

		for (IJARView view : views) {
			view.forceUpdate();
		}
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("[BasicJARModel]:\n");
		sb.append("\t[Mode]: ").append(mode).append('\n');
		sb.append("\t").append(menu).append('\n');
		sb.append("\t[Selection]: ").append(selection).append(":");
		sb.append(menu.get(selection)).append("\n");
		sb.append("\t[Sneaking]: ").append(isSneaking()).append("\n");
		sb.append("\t[Player-Y]: ").append(player_y_pos).append("\n");
		sb.append("\t[Time]: ").append(gameTime).append("\n");
		return sb.toString();
	}

	private class GameLoop implements Runnable {

		public void run() {

			while (running) {
				while (mode == Mode.GAME) {
					gameTime++;
					if (player_y_pos != 0 || player_y_velocity > 0) {
						player_y_pos += player_y_velocity;
						player_y_velocity -= 0.5;
					}
					if (player_y_pos < 0) {
						player_y_pos = 0;
					}

					//TODO splayer should be stable and all hurdles should move towards the player

					try {
						synchronized (this) {
							this.wait(50);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				try {
					synchronized (this) {
						this.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
