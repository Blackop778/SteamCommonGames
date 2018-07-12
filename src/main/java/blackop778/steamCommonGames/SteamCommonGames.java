package blackop778.steamCommonGames;

import blackop778.steamCommonGames.graphics.SetupPanel;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class SteamCommonGames {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            System.err.println("Could not load System Look and Feel (What makes the UI pretty). Reason: ");
            e.printStackTrace();
        }

        // Pass null object to make JFrame appear in task bar
        JFrame frame = new JFrame("Steam Common Games",null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        SetupPanel panel = new SetupPanel(frame);
        frame.setContentPane(panel);
        frame.setVisible(true);
        frame.pack();
        frame.setResizable(true);
    }
}
