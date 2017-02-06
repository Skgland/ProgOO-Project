package de.webtwob.view;

import de.webtwob.interfaces.IJARLinkable;
import de.webtwob.interfaces.IJARModel;

import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * @author Bennet Blessmann
 *         Created on 05.02.2017.
 */
public class GameField extends Canvas implements Runnable, IJARLinkable {

	BufferStrategy bs;

	private IJARModel model;

	@Override
	public void linkModel(IJARModel ijarm) {

		model = ijarm;
	}

	private double player_y, win_player_y;
	private int[][] playerBlocks;
	private double  player_height, win_player_height;
	private Graphics graphics;
	double win_height;
	double win_width;
	public GameField() {

		setBackground(Color.BLUE);
		setIgnoreRepaint(true);
	}

	@Override
	public void addNotify() {

		super.addNotify();
		if (bs == null) {
			createBufferStrategy(2);
			bs = getBufferStrategy();
		}
	}

	@Override
	public void run() {

		if (model != null && bs != null) {
			//Prepwork
			win_width = getWidth() / 28;
			win_height = getHeight() / 14;
			player_y = model.getPlayerY();
			player_height = model.getPlayerHeight();

			win_player_height = win_height * player_height;
			win_player_y = win_height * player_y;

			Rectangle[] hurdels = model.getHurdels();

			//get Graphics every cycle to ensure validity
			graphics = bs.getDrawGraphics();

			//drawBackground
			graphics.setColor(Color.BLUE);
			graphics.fillRect(0, 0, getWidth(), getHeight());

			//drawFlor
			graphics.setColor(Color.GRAY);
			graphics.fillRect(0, getHeight() - (int) (2 * win_height), getWidth(), (int) win_height);

			graphics.setColor(Color.CYAN);
			Rectangle rect;
			for (int i = 0; i < hurdels.length; i++) {
				if ((rect = hurdels[i]) != null) {
					graphics.fillRect(
							(int) ((i - 1) * win_width),
							(int) (getHeight()-((rect.getY()+rect.getHeight()+2)*win_height)),
							(int) (rect.getWidth() * win_width),
							(int) (rect.getHeight() * win_height)
					);
				}
			}

			//drawPlayer
			graphics.setColor(Color.YELLOW);
			graphics.fillRect((int) win_width,
			                  getHeight() - ((int) (2 * win_height) + (int) (win_player_height) + (int) (win_player_y)),
			                  (int) win_width,
			                  (int) (win_player_height));

			graphics.setColor(Color.RED);
			graphics.drawString("Time: " + model.getTime(), 20, 20);
			graphics.drawString("Score: " + model.getScore(), 20, 40);

			if (!bs.contentsLost()) {
				//display buffer
				bs.show();
			}
			Toolkit.getDefaultToolkit().sync();
			if (graphics != null) {
				//dispose of graphics
				graphics.dispose();
			}
		}
	}
}
