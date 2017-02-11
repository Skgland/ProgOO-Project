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

	private BufferStrategy bs;
	private IJARModel      model;

	public GameField() {

		setBackground(Color.BLUE);
		setIgnoreRepaint(true);
	}
	@Override
	public void linkModel(final IJARModel ijarm) {

		model = ijarm;
	}
	@Override
	public void addNotify() {

		super.addNotify();
		createBufferStrategy(2);
		bs = getBufferStrategy();
	}

	@Override
	public void run() {

		if (model != null && bs != null) {
			//Preparation work
			final double win_width  = getWidth() / 28;
			final double win_height = getHeight() / 14;

			final double player_y      = model.getPlayerY();
			final double player_height = model.getPlayerHeight();

			final double win_player_height = win_height * player_height;
			final double win_player_y      = win_height * player_y;

			final Rectangle[] hurdles = model.getHurdles();

			//get Graphics every cycle to ensure validity
			final Graphics graphics = bs.getDrawGraphics();

			//drawBackground
			graphics.setColor(Color.BLUE);
			graphics.fillRect(0, 0, getWidth(), getHeight());

			//drawFlor
			graphics.setColor(Color.GRAY);
			graphics.fillRect(0, getHeight() - (int) (2 * win_height), getWidth(), (int) win_height);

			graphics.setColor(Color.CYAN);
			Rectangle rect;
			for (int i = 0; i < hurdles.length; i++) {
				rect = hurdles[i];
				if (rect != null) {
					graphics.fillRect(
							(int) ((i - 1) * win_width),
							(int) (getHeight() - ((rect.getY() + rect.getHeight() + 2) * win_height)),
							(int) (rect.getWidth() * win_width),
							(int) (rect.getHeight() * win_height)
					);
				}
			}

			//drawPlayer
			graphics.setColor(Color.YELLOW);
			graphics.fillRect(
					(int) win_width,
					getHeight() - ((int) (2 * win_height) + (int) (win_player_height) + (int) (win_player_y)),
					(int) win_width,
					(int) (win_player_height)
			);

			graphics.setColor(Color.RED);
			graphics.drawString("Time: " + model.getTime(), 20, 20);
			graphics.drawString("Score: " + model.getScore(), 20, 40);

			if (!bs.contentsLost()) {
				//display buffer
				bs.show();
			}
			Toolkit.getDefaultToolkit().sync();
			//dispose of graphics
			graphics.dispose();
		}
	}

	@Override
	public String toString() {

		return "A GameField Object";
	}
}
