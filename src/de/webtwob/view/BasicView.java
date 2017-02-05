package de.webtwob.view;

import de.webtwob.interfaces.IJARModel;
import de.webtwob.interfaces.IJARView;
import de.webtwob.interfaces.IMenuEntry;
import de.webtwob.model.GameField;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BB20101997 on 31. Jan. 2017.
 */
public class BasicView extends JPanel implements IJARView {

	private          IJARModel model;
	private          Thread    exec;
	private volatile boolean   running;
	private final Object waiter = new Object();
	private IJARModel.Mode               mode        = null;
	private int                          select      = -1;
	private List<IMenuEntry>             menuEntries = new ArrayList<>();
	private DefaultListModel<IMenuEntry> listModel   = new DefaultListModel<>();
	private GameField            gameField = new GameField();

	private JList<IMenuEntry> list      = new JList<>(listModel);

	public BasicView() {

		list.setCellRenderer(new MenuEntryListCellRendere());
		setVisible(true);
		setLayout(new BorderLayout());
		list.addListSelectionListener(
				(ListSelectionEvent e) -> {
					if (select != list.getSelectedIndex()) {
						synchronized (waiter) {
							model.select(select = list.getSelectedIndex());
						}
					}
				}
		);
		list.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int       index = list.locationToIndex(e.getPoint());
				listModel.getElementAt(index).executeAction();
			}

			@Override
			public void mousePressed(MouseEvent e) {

			}
			@Override
			public void mouseReleased(MouseEvent e) {

			}
			@Override
			public void mouseEntered(MouseEvent e) {

			}
			@Override
			public void mouseExited(MouseEvent e) {

			}
		});
		setMinimumSize(getPreferredSize());
		updateUI();

	}

	private final Runnable runner = this::keepAlive;

	private void keepAlive() {
		boolean changed;
		while (running) {
			try {
				if (model != null&&model.getMode()!=null) {
					changed = false;
					if (model.getMode() == IJARModel.Mode.GAME) {
						if (mode != IJARModel.Mode.GAME) {
							removeAll();
							add(gameField,BorderLayout.CENTER);
							mode = IJARModel.Mode.GAME;
							updateUI();
						}
						gameField.run();
						synchronized (runner) {
							runner.wait(10);
						}
					} else {
						if (mode != IJARModel.Mode.MENU) {
							removeAll();
							add(list,BorderLayout.CENTER);
							mode = IJARModel.Mode.MENU;
							changed = true;
						}
						if (!menuEntries.equals(model.getMenuEntries())) {
							menuChanged();
							changed = true;
						}
						if (select != model.getSelectedIndex()) {
							synchronized (waiter) {
								select = model.getSelectedIndex();
							}
							changed = true;
						}
						if (select != list.getSelectedIndex()) {
							list.setSelectedIndex(select);
							changed = true;
						}
						if(changed) {
							updateUI();
						}
						synchronized (runner) {
							runner.wait(10);
						}
					}
				} else {
					System.out.println(""+model+((model!=null)?model.getMode():""));
					synchronized (runner) {
						runner.wait(100);
					}
				}
			} catch (InterruptedException ignored) {
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private void menuChanged() {
		menuEntries = model.getMenuEntries();
		listModel.clear();
		for (IMenuEntry ime : menuEntries) {
			listModel.addElement(ime);
		}
	}

	@Override
	public void linkModel(IJARModel ijarm) {
		model = ijarm;
		gameField.linkModel(model);
	}

	@Override
	public void start() {

		if (exec == null) {
			running = true;
			exec = new Thread(runner);
			exec.setName("Basic-View-Updater");
			exec.start();
		}
	}

	@Override
	public void stop() {
		running = false;
		synchronized (runner) {
			runner.notifyAll();
		}
		exec = null;
	}

	@Override
	public void forceUpdate() {
		updateUI();
	}
}
