package blackop778.steamCommonGames;

import javax.swing.JFrame;

import blackop778.steamCommonGames.graphics.SetupPanel;

public class SteamCommonGames {

    public static void main(String[] args) {
	JFrame frame = new JFrame("Enter the Steam IDs");
	frame.setContentPane(new SetupPanel());
	frame.setVisible(true);
	frame.pack();
    }

}
