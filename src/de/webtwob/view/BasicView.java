package de.webtwob.view;

import de.webtwob.interfaces.IJARModel;
import de.webtwob.interfaces.IJARView;
import de.webtwob.interfaces.IMenuEntry;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bennet Blessmann
 * Created on 31. Jan. 2017.
 */
public class BasicView extends JPanel implements IJARView {

	/**
	 * The linked Model
	 * */
	private          IJARModel model;

	/**
	 * The thread to update this view
	 * */
	private          Thread    exec;

	/**
	 * if exec should continue running
	 * */
	private volatile boolean   running;

	/**
	 * Used to synchronise on
	 * */
	private final Object waiter = new Object();

	/**
	 * Used to store the latest selected value so we only update on change
	 * */
	private int                          select      = -1;

	/**
	 * Stores the currently in use menuEntries
	 * */
	private List<IMenuEntry>             menuEntries = new ArrayList<>();

	/**
	 * Stores the used listModel
	 * */
	private final DefaultListModel<IMenuEntry> listModel = new DefaultListModel<>();

	/**
	 * Stores the reused GameField
	 * */
	private final GameField gameField = new GameField();

	@Override
	public String toString() {
		return "BasicView "+(model!=null?"linked to "+model:"not linked to a Model.");
	}

	/**
	 * Stores the re-used JList
	 * */
	private final JList<IMenuEntry> list = new JList<>(listModel);

	public BasicView() {

		//set the cell renderer for the list to our cell renderer
		list.setCellRenderer(new MenuEntryListCellRenderer());
		setLayout(new BorderLayout());

		//add a SelectionListener to update the model when the user selects an entry via the GUI
		list.addListSelectionListener(
				(ListSelectionEvent e) -> {
					if (select != list.getSelectedIndex()) {
						//stop the update thread from undoing the change be for it is submitted
						synchronized (waiter) {
							select = list.getSelectedIndex();
							model.select(select);
						}
					}
				}
		);
		setMinimumSize(getPreferredSize());
		//to make the buttons in the list "work"
		list.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				listModel.getElementAt(list.locationToIndex(e.getPoint())).executeAction();
			}

			@Override
			public void mousePressed(final MouseEvent e) {

			}
			@Override
			public void mouseReleased(final MouseEvent e) {

			}
			@Override
			public void mouseEntered(final MouseEvent e) {

			}
			@Override
			public void mouseExited(final MouseEvent e) {

			}
		});
		setVisible(true);
		updateUI();
	}

	private final Runnable runner = this::keepAlive;

	private void keepAlive() {
		boolean        changed;
		IJARModel.Mode mode = null;
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
							// w/o synchronize would override clicks by the user
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
					synchronized (runner) {
						runner.wait(100);
					}
				}
			} catch (final InterruptedException ignored) {
				//expected from the waits
			} catch (final Exception e) {
				/*
				 * probably missed a null check
				 * still the view should just keep updating
				 * no matter what happens
				*/
				e.printStackTrace();
			}
		}
	}
	private void menuChanged() {
		menuEntries = model.getMenuEntries();
		listModel.clear();
		for (final IMenuEntry ime : menuEntries) {
			listModel.addElement(ime);
		}
	}

	@Override
	public void linkModel(final IJARModel ijarm) {
		model = ijarm;
		gameField.linkModel(model);
	}

	/**
	 * Start the update Thread
	 * */
	@Override
	public void start() {
		if (exec == null) {
			running = true;
			exec = new Thread(runner);
			exec.setName("Basic-View-Updater");
			exec.start();
		}
	}

	/**
	 * stop the update Thread
	 * */
	@Override
	public void stop() {
		running = false;
		synchronized (runner) {
			runner.notifyAll();
		}
		try {
			//wait for the thread to yield
			exec.join();
		} catch (final InterruptedException ignore) {
		}
		exec = null;
	}

	/**
	 * handel a forced update
	 * usually when a MenuEntry changes it's Text
	 * to get the view to update
	 * */
	@Override
	public void forceUpdate() {
		updateUI();
	}
}
