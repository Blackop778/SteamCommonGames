package blackop778.steamCommonGames;

import blackop778.steamCommonGames.graphics.SetupPanel;

import javax.swing.*;

public class SteamCommonGames {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Enter the Steam IDs");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        SetupPanel panel = new SetupPanel(frame);
        frame.setContentPane(panel);
        frame.setVisible(true);
        frame.pack();
        frame.setResizable(true);
    }

}
