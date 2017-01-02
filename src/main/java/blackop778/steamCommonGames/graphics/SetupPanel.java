package blackop778.steamCommonGames.graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import blackop778.steamCommonGames.XMLParser;
import blackop778.steamCommonGames.XMLParser.Game;

public class SetupPanel extends JPanel {
    private static final long serialVersionUID = 3610537775428944030L;

    private ArrayList<InputSet> fields = new ArrayList<>();
    public JFrame parent;
    static int count = 0;
    static DoneButton done;

    public SetupPanel(JFrame parent) {
	this.parent = parent;
	setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	fields.add(new InputSet(this));
	fields.add(new InputSet(this));
	ButtonPanel buttonPanel = new ButtonPanel(this);
	add(buttonPanel);
    }

    public static class ButtonPanel extends JPanel {
	private static final long serialVersionUID = 3319311564335045936L;
	public SetupPanel parent;

	public ButtonPanel(SetupPanel parent) {
	    this.parent = parent;
	    AddRemoveInfoButton add = new AddRemoveInfoButton(true, this);
	    add(add);
	    AddRemoveInfoButton remove = new AddRemoveInfoButton(false, this);
	    add(remove);
	    add.setBrother(remove);
	    add(new DoneButton(this));
	}
    }

    public static class DoneButton extends JButton {
	private static final long serialVersionUID = 5582335127021262021L;
	private ButtonPanel parent;

	public DoneButton(ButtonPanel parent) {
	    this.parent = parent;
	    done = this;
	    checkIfReady();
	    setText("Done");
	    addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
		    try {
			Game[] games = XMLParser
				.parse2XML(
					new URL("https://steamcommunity.com/id/"
						+ parent.parent.fields.get(0).text.getText() + "/games?xml=1"),
					new URL("https://steamcommunity.com/id/"
						+ parent.parent.fields.get(1).text.getText() + "/games?xml=1"))
				.toArray(new XMLParser.Game[] {});
			System.out.println("https://steamcommunity.com/id/" + parent.parent.fields.get(0).text.getText()
				+ "/games?xml=1");
			System.out.println("https://steamcommunity.com/id/" + parent.parent.fields.get(1).text.getText()
				+ "/games?xml=1");
			parent.parent.parent.dispose();
			String answer = "Common games are:\n";
			for (Game game : games) {
			    answer += game.name + "\n";
			}
			JOptionPane.showMessageDialog(null, answer, "Common games", JOptionPane.INFORMATION_MESSAGE);
		    } catch (MalformedURLException e) {
			e.printStackTrace();
		    }
		}
	    });
	}

	public void checkIfReady() {
	    for (int i = 0; i < parent.parent.fields.size(); i++) {
		InputSet current = parent.parent.fields.get(i);
		if (current.check == null || current.check.isEnabled()) {
		    if (current.text.getText().isEmpty()) {
			setEnabled(false);
			return;
		    }
		} else {
		    setEnabled(false);
		    return;
		}
	    }
	    setEnabled(true);
	}
    }

    public static class AddRemoveInfoButton extends JButton {
	private static final long serialVersionUID = -3085228228149776981L;

	@SuppressWarnings("unused")
	private boolean add;
	@SuppressWarnings("unused")
	private ButtonPanel parent;
	private AddRemoveInfoButton brother;

	public void setBrother(AddRemoveInfoButton brother) {
	    if (this.brother == null) {
		this.brother = brother;
	    }
	}

	public AddRemoveInfoButton(boolean add, ButtonPanel parent) {
	    this.add = add;
	    this.parent = parent;
	    setText(add ? "+" : "-");
	    if (add) {
		addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
			parent.parent.fields.add(new InputSet(parent.parent));
			parent.parent.revalidate();
			parent.parent.repaint();
			parent.parent.parent.pack();
			if (parent.parent.fields.size() == 3) {
			    brother.setEnabled(true);
			}
			ButtonPanel newPanel = new ButtonPanel(parent.parent);
			parent.parent.add(newPanel);
			parent.parent.remove(parent);
			done.setEnabled(false);
		    }
		});
	    } else {
		if (parent.parent.fields.size() == 2) {
		    setEnabled(false);
		}
		addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent arg0) {
			InputSet set = parent.parent.fields.get(parent.parent.fields.size() - 1);
			parent.parent.remove(set.panel);
			parent.parent.fields.remove(set);
			parent.parent.repaint();
			parent.parent.parent.pack();
			if (parent.parent.fields.size() == 2) {
			    setEnabled(false);
			}
			done.checkIfReady();
		    }
		});
	    }
	}
    }
}
