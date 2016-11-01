package blackop778.steamCommonGames.graphics;

import java.util.ArrayList;

import javax.swing.JPanel;

public class SetupPanel extends JPanel {
    private static final long serialVersionUID = 3610537775428944030L;
    
    private ArrayList<InputSet> fields = new ArrayList<InputSet>();
    
    public SetupPanel() {
	fields.add(new InputSet(this));
	fields.add(new InputSet(this));
    }

}
