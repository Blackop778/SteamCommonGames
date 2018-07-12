package blackop778.steamCommonGames.graphics;

import blackop778.steamCommonGames.CommonGamesWorker;
import blackop778.steamCommonGames.XMLParser.Game;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SetupPanel extends JPanel {
    private static final long serialVersionUID = 3610537775428944030L;

    private ArrayList<InputSet> fields = new ArrayList<>();
    private JFrame frame;
    static int count = 0;
    static DoneButton done;

    public SetupPanel(JFrame frame) {
        this.frame = frame;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        fields.add(new InputSet(this));
        fields.add(new InputSet(this));
        ButtonPanel buttonPanel = new ButtonPanel(this);
        add(buttonPanel);
    }

    // Todo Implement https://developer.valvesoftware.com/wiki/Steam_browser_protocol to launch games
    public void displayCommonGames(Game[] games) {
        StringBuilder displayString = new StringBuilder("Common games are:");

        for(int i = 0; i + 1 < games.length; i += 2) {
            String intermediate1 = games[i].name + " - " + games[i].appID;
            String intermediate2 = games[i + 1].name + " - " + games[i + 1].appID ;
            displayString.append(String.format("\n%-66s    %s", intermediate1, intermediate2));
        }

        if(games.length % 2 == 1) {
            displayString.append("\n").append(games[games.length - 1].name).append(" - ").append(games[games.length - 1].appID);
        }

        frame.setContentPane(new JScrollPane(new JTextArea(displayString.toString())));
        frame.pack();
    }

    public static class ButtonPanel extends JPanel {
        private static final long serialVersionUID = 3319311564335045936L;
        SetupPanel setupPanel;

        ButtonPanel(SetupPanel setupPanel) {
            this.setupPanel = setupPanel;
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
        private ButtonPanel buttonPanel;

        DoneButton(ButtonPanel buttonPanel) {
            this.buttonPanel = buttonPanel;
            done = this;
            checkIfReady();
            setText("Done");
            addActionListener(arg0 -> {
                try {
                    buttonPanel.setupPanel.removeAll();
                    JLabel comparingText = new JLabel("<html><h1>Comparing games...</html></h1>");
                    buttonPanel.setupPanel.add(comparingText);
                    buttonPanel.setupPanel.frame.pack();
                    CommonGamesWorker task = new CommonGamesWorker(buttonPanel.setupPanel,
                            new URL("https://steamcommunity.com/id/"
                                    + buttonPanel.setupPanel.fields.get(0).text.getText() + "/games?xml=1"),
                            new URL("https://steamcommunity.com/id/"
                                    + buttonPanel.setupPanel.fields.get(1).text.getText() + "/games?xml=1")
                    );
                    task.execute();
                } catch (MalformedURLException e) {
                    System.err.println("Error in URL Syntax: ");
                    e.printStackTrace();
                }
            });
        }

        void checkIfReady() {
            for (int i = 0; i < buttonPanel.setupPanel.fields.size(); i++) {
                InputSet current = buttonPanel.setupPanel.fields.get(i);
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

        void setBrother(AddRemoveInfoButton brother) {
            if (this.brother == null) {
                this.brother = brother;
            }
        }

        AddRemoveInfoButton(boolean add, ButtonPanel parent) {
            this.add = add;
            this.parent = parent;
            setText(add ? "+" : "-");
            if (add) {
                addActionListener(e -> {
                    parent.setupPanel.fields.add(new InputSet(parent.setupPanel));
                    parent.setupPanel.revalidate();
                    parent.setupPanel.repaint();
                    parent.setupPanel.frame.pack();
                    if (parent.setupPanel.fields.size() == 3) {
                        brother.setEnabled(true);
                    }
                    ButtonPanel newPanel = new ButtonPanel(parent.setupPanel);
                    parent.setupPanel.add(newPanel);
                    parent.setupPanel.remove(parent);
                    done.setEnabled(false);
                });
            } else {
                if (parent.setupPanel.fields.size() == 2) {
                    setEnabled(false);
                }
                addActionListener(arg0 -> {
                    InputSet set = parent.setupPanel.fields.get(parent.setupPanel.fields.size() - 1);
                    parent.setupPanel.remove(set.panel);
                    parent.setupPanel.fields.remove(set);
                    parent.setupPanel.repaint();
                    parent.setupPanel.frame.pack();
                    if (parent.setupPanel.fields.size() == 2) {
                        setEnabled(false);
                    }
                    done.checkIfReady();
                });
            }
        }
    }
}
