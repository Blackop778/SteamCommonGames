package blackop778.steamCommonGames.graphics;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class InputSet {
    public JCheckBox check;
    public JTextField text;
    public JPanel panel;

    public InputSet(SetupPanel parentPanel) {
        panel = new JPanel();
        parentPanel.add(panel);
        if (SetupPanel.count > 1) {
            check = new SetupCheckBox("", true, this);
            panel.add(check);
        }
        SetupPanel.count++;
        text = new JTextField(20);
        text.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                SetupPanel.done.checkIfReady();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                SetupPanel.done.checkIfReady();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                SetupPanel.done.checkIfReady();
            }
        });
        panel.add(text);
    }

    public static class SetupCheckBox extends JCheckBox {
        private static final long serialVersionUID = 8707337272289468667L;

        @SuppressWarnings("unused")
        private InputSet parent;

        public SetupCheckBox(String text, boolean selected, InputSet parent) {
            super(text, selected);
            this.parent = parent;
            addActionListener(arg0 -> parent.text.setEnabled(!parent.text.isEnabled()));
        }
    }
}
