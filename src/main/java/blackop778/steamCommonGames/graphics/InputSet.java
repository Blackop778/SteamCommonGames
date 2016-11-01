package blackop778.steamCommonGames.graphics;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InputSet {
    public JCheckBox check;
    public JTextField text;
    
    public InputSet(JPanel panel) {
	check = new JCheckBox("", true);
	panel.add(check);
	text = new JTextField(20);
	panel.add(text);
    }
}
