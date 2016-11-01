package blackop778.steamCommonGames;

import javax.swing.JFrame;

import blackop778.steamCommonGames.graphics.SetupPanel;

public class SteamCommonGames {

    public static void main(String[] args) {
	JFrame frame = new JFrame("Enter the Steam IDs");
	SetupPanel panel = new SetupPanel();
	frame.setContentPane(panel);
	frame.setVisible(true);
	frame.pack();
    }

}
