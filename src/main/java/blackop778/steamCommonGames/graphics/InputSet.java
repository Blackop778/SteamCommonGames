package blackop778.steamCommonGames.graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InputSet {
    public JCheckBox check;
    public JTextField text;
    public JPanel panel;
    
    public InputSet(SetupPanel parentPanel) {
	panel = new JPanel();
	parentPanel.add(panel);
	check = new SetupCheckBox("", true, this);
	panel.add(check);
	text = new JTextField(20);
	panel.add(text);
    }
    
    public static class SetupCheckBox extends JCheckBox {
	private static final long serialVersionUID = 8707337272289468667L;
	
	@SuppressWarnings("unused")
	private InputSet parent;
	
	public SetupCheckBox(String text, boolean selected, InputSet parent) {
	    super(text, selected);
	    this.parent = parent;
	    addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
		    parent.text.setEnabled(!parent.text.isEnabled());
		}});
	}
    }
}
