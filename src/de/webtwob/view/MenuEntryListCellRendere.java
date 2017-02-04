package de.webtwob.view;

import de.webtwob.interfaces.IMenuEntry;

import javax.swing.*;
import java.awt.*;

/**
 * @author Bennet Blessmann
 *         Created on 04.02.2017.
 */
public class MenuEntryListCellRendere implements ListCellRenderer<IMenuEntry> {

	@Override
	public Component getListCellRendererComponent(JList<? extends IMenuEntry> list, IMenuEntry value, int index, boolean isSelected, boolean cellHasFocus) {

		JPanel jp = new JPanel();
		JButton but = new JButton(value.getText());
		//but.addActionListener((ActionEvent e)->value.executeAction());
		jp.setLayout(new BoxLayout(jp, BoxLayout.LINE_AXIS));
		Box b = Box.createHorizontalBox();
		b.add(but);
		b.add(Box.createHorizontalGlue());
		if (isSelected) {
			but.setSelected(true);
			jp.setBackground(Color.LIGHT_GRAY);
		}
		if (value.getValue() == null) {
			jp.add(b);
			return jp;
		} else {
			but.setText(but.getText()+":");
			b.add(new JLabel(value.getValue()));
			jp.add(b);
			return jp;
		}
	}
}
