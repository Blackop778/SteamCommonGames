package blackop778.steamCommonGames.graphics;

import blackop778.steamCommonGames.XMLParser;
import blackop778.steamCommonGames.XMLParser.Game;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.awt.Container;
import javax.swing.JButton;
import javax.swing.JOptionPane;
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

    void displayCommonGames(Game[] games) {
        frame.dispose();
        StringBuilder answer = new StringBuilder("Common games are:\n");
        for (Game game : games) {
            answer.append(game.name).append("\n");
        }
        // TODO: Display in JFrame and use column-width property
        JOptionPane.showMessageDialog(null, answer.toString(), "Common games", JOptionPane.INFORMATION_MESSAGE);
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
                    JLabel comparingText = new JLabel("Comparing games...");
                    JPanel comparingPanel = new JPanel();
                    comparingPanel.add(comparingText);
                    buttonPanel.setupPanel.frame.setContentPane(comparingPanel);
                    buttonPanel.setupPanel.frame.validate();
                    // TODO: Move FindCommonGames to SwingWorker
                    ArrayList<Game> arrayList = XMLParser
                            .FindCommonGames(
                                    new URL("https://steamcommunity.com/id/"
                                            + buttonPanel.setupPanel.fields.get(0).text.getText() + "/games?xml=1"),
                                    new URL("https://steamcommunity.com/id/"
                                            + buttonPanel.setupPanel.fields.get(1).text.getText() + "/games?xml=1"));
                    if(arrayList == null) {
                        buttonPanel.setupPanel.frame.setContentPane((Container) (new JPanel().add(new JLabel("Error while comparing games (null ArrayList returned)"))));
                        return;
                    }
                    Game[] games = arrayList.toArray(new Game[]{});
                    buttonPanel.setupPanel.displayCommonGames(games);
                } catch (MalformedURLException e) {
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
