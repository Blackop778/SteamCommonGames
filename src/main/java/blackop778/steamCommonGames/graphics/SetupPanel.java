package blackop778.steamCommonGames.graphics;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SetupPanel extends JPanel {
    private static final long serialVersionUID = 3610537775428944030L;

    private ArrayList<InputSet> fields = new ArrayList<>();
    public JFrame parent;
    static int count = 0;

    public SetupPanel(JFrame parent) {
	this.parent = parent;
	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	fields.add(new InputSet(this));
	fields.add(new InputSet(this));
	JPanel buttonPanel = new JPanel();
	AddRemoveInfo add = new AddRemoveInfo(true, this);
	buttonPanel.add(add);
	AddRemoveInfo remove = new AddRemoveInfo(false, this);
	buttonPanel.add(remove);
	add.setBrother(remove);
	add(buttonPanel, BorderLayout.PAGE_END);
    }

    public static class AddRemoveInfo extends JButton {
	private static final long serialVersionUID = -3085228228149776981L;

	@SuppressWarnings("unused")
	private boolean add;
	@SuppressWarnings("unused")
	private SetupPanel parent;
	private AddRemoveInfo brother;

	public void setBrother(AddRemoveInfo brother) {
	    if (this.brother == null)
		this.brother = brother;
	}

	public AddRemoveInfo(boolean add, SetupPanel parent) {
	    this.add = add;
	    this.parent = parent;
	    setText(add ? "+" : "-");
	    if (add)
		addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
			parent.fields.add(new InputSet(parent));
			parent.revalidate();
			parent.repaint();
			parent.parent.pack();
			if (parent.fields.size() == 3)
			    brother.setEnabled(true);
		    }
		});
	    else {
		setEnabled(false);
		addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent arg0) {
			InputSet set = parent.fields.get(parent.fields.size() - 1);
			parent.remove(set.panel);
			parent.fields.remove(set);
			parent.repaint();
			parent.parent.pack();
			if (parent.fields.size() == 2)
			    setEnabled(false);
		    }
		});
	    }
	}
    }
}
