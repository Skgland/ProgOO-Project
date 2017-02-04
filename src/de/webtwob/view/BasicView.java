package de.webtwob.view;

import de.webtwob.interfaces.IJARModel;
import de.webtwob.interfaces.IJARView;
import de.webtwob.interfaces.IMenuEntry;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
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

	private JList<IMenuEntry> list = new JList<>(listModel);

	public BasicView() {

		list.setCellRenderer(new MenuEntryListCellRendere());
		setVisible(true);
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
		updateUI();
	}

	private final Runnable runner = this::keepAlive;

	private void keepAlive() {
		boolean changed;
		while (running) {
			try {
				if (model != null) {
					changed = false;
					if (model.getMode() == IJARModel.Mode.GAME) {
						if (mode != IJARModel.Mode.GAME) {
							removeAll();
							//ADD game
							mode = IJARModel.Mode.GAME;
							changed = true;
						}
						//TODO update game
					} else {
						if (mode != IJARModel.Mode.MENU) {
							removeAll();
							add(list);
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
							runner.wait(100);
						}
					}
				} else {
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
