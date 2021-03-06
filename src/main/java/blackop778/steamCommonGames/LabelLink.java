package blackop778.steamCommonGames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

/**
 * Code adapted from http://stackoverflow.com/a/8387251
 */
public class LabelLink extends JLabel {
    private static final long serialVersionUID = -4786471631601504534L;
    private static final String A_HREF = "<a href=\"";
    private static final String HREF_CLOSED = "\">";
    private static final String HREF_END = "</a>";
    private static final String HTML = "<html>";
    private static final String HTML_END = "</html>";

    public LabelLink(String textToDisplay, String link) {
        setText(textToDisplay);
        if (isBrowsingSupported()) {
            makeLinkable(this, new LinkMouseListener());
        }
    }

    private static void makeLinkable(JLabel c, MouseListener ml) {
        assert ml != null;
        c.setText(htmlIfy(linkIfy(c.getText())));
        c.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        c.addMouseListener(ml);
    }

    private static boolean isBrowsingSupported() {
        if (!Desktop.isDesktopSupported()) {
            return false;
        }
        boolean result = false;
        Desktop desktop = java.awt.Desktop.getDesktop();
        if (desktop.isSupported(Desktop.Action.BROWSE)) {
            result = true;
        }
        return result;

    }

    private static class LinkMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            JLabel l = (JLabel) evt.getSource();
            try {
                // TODO Add Confirmation JOptionPane
                URI uri = new java.net.URI(LabelLink.getPlainLink(l.getText()));
                (new LinkRunner(uri)).execute();
            } catch (URISyntaxException use) {
                throw new AssertionError(use + ": " + l.getText()); //NOI18N
            }
        }
    }

    private static class LinkRunner extends SwingWorker<Void, Void> {

        private final URI uri;

        private LinkRunner(URI u) {
            if (u == null) {
                throw new NullPointerException();
            }
            uri = u;
        }

        @Override
        protected Void doInBackground() throws Exception {
            Desktop desktop = java.awt.Desktop.getDesktop();
            desktop.browse(uri);
            return null;
        }

        @Override
        protected void done() {
            try {
                get();
            } catch (ExecutionException | InterruptedException error) {
                handleException(uri, error);
            }
        }

        private static void handleException(URI u, Exception e) {
            JOptionPane.showMessageDialog(null, "Sorry, a problem occurred while trying to open this link in your system's standard browser.", "A problem occurred", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String getPlainLink(String s) {
        return s.substring(s.indexOf(A_HREF) + A_HREF.length(), s.indexOf(HREF_CLOSED));
    }

    //WARNING
    //This method requires that s is a plain string that requires
    //no further escaping
    private static String linkIfy(String s) {
        return A_HREF.concat(s).concat(HREF_CLOSED).concat(s).concat(HREF_END);
    }

    //WARNING
    //This method requires that s is a plain string that requires
    //no further escaping
    private static String htmlIfy(String s) {
        return HTML.concat(s).concat(HTML_END);
    }
}
