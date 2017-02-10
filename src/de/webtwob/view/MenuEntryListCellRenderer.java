package de.webtwob.view;

import de.webtwob.interfaces.IMenuEntry;

import javax.swing.*;
import java.awt.*;

/**
 * @author Bennet Blessmann
 *         Created on 04.02.2017.
 */
class MenuEntryListCellRenderer implements ListCellRenderer<IMenuEntry> {

	@Override
	public Component getListCellRendererComponent(final JList<? extends IMenuEntry> list,
	                                              final IMenuEntry value,
	                                              final int index,
	                                              final boolean isSelected,
	                                              final boolean cellHasFocus) {

		final JPanel  jp  = new JPanel();
		final JButton but = new JButton(value.getText());
		//but.addActionListener((ActionEvent e)->value.executeAction());
		jp.setLayout(new BoxLayout(jp, BoxLayout.LINE_AXIS));
		final Box box = Box.createHorizontalBox();
		box.add(but);
		box.add(Box.createHorizontalGlue());
		if (isSelected) {
			but.setSelected(true);
			jp.setBackground(Color.LIGHT_GRAY);
		}
		if (value.getValue() == null) {
			jp.add(box);
			return jp;
		} else {
			but.setText(but.getText()+":");
			box.add(new JLabel(value.getValue()));
			jp.add(box);
			return jp;
		}
	}
}
