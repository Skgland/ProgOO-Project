package de.webtwob.model;

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

	private Color[][] windows = new Color[29][15];
	private double   player_y,win_player_y;
	private int[][] playerBlocks;
	private double   player_height,win_player_height;
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
			win_width  = getWidth() / 28;
			win_height = getHeight() / 14;
			player_y = model.getPlayerY();
			player_height = model.getPlayerHeight();

			win_player_height = win_height*player_height;
			win_player_y = win_height* player_y;

			for (int x = 0; x < 29; x++) {
				for (int y = 0; y < 15; y++) {
					if ((x == 1) && (y >= (int)player_y+2) && (y <= (int)(player_y + player_height+2))) {
						windows[x][y] = Color.YELLOW;
					} else if(y==1) {
						windows[x][y] = Color.GRAY;
					}else {
						windows[x][y] = Color.BLUE;
					}
				}
			}

			//get Graphics every cycle to ensure validity
			graphics = bs.getDrawGraphics();

			//drawBackground
			graphics.setColor(Color.BLUE);
			graphics.fillRect(0,0,getWidth(),getHeight());

			//drawFlor
			graphics.setColor(Color.GRAY);
			graphics.fillRect(0,getHeight()-(int)(2*win_height),getWidth(),(int)win_height);

			//drawPlayer
			graphics.setColor(Color.YELLOW);
			graphics.fillRect((int)win_width,getHeight()-((int)(2*win_height)+(int)(win_player_height)+(int)(win_player_y)),(int)win_width,(int)(win_player_height));

			graphics.setColor(Color.RED);
			graphics.drawString("Time: "+model.getTime(),20,20);
			graphics.drawString("Score: "+model.getScore(),20,40);

			if (!bs.contentsLost())
			{
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
