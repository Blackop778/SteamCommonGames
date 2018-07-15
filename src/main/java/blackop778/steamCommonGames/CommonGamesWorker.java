package blackop778.steamCommonGames;

import blackop778.steamCommonGames.XMLParser.Game;
import blackop778.steamCommonGames.graphics.SetupPanel;

import javax.swing.SwingWorker;
import javax.xml.stream.XMLStreamException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CommonGamesWorker extends SwingWorker<Game[], Game> {
    private final SetupPanel panel;
    private final URL xml1;
    private final URL xml2;
    private final URL[] xmls;

    public CommonGamesWorker(SetupPanel panel, URL xml1, URL xml2, URL[] xmls) {
        this.panel = panel;
        this.xml1 = xml1;
        this.xml2 = xml2;
        this.xmls = xmls;
    }

    @Override
    protected Game[] doInBackground() {
        try {
            return XMLParser.findCommonGames(this, xml1, xml2, xmls);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (XMLParser.ReaderCancelledException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

        return new Game[] {};
    }

    @Override
    protected void done() {
        try {
            panel.displayCommonGames(get());
        } catch (InterruptedException e) {
            System.err.println("Comparison was interrupted. Reason: ");
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.err.println("Encountered an Error while comparing games: ");
            e.printStackTrace();
        }
    }

    void publishGame(Game game) {
        publish(game);
    }

    @Override
    protected void process(List<Game> chunks) {
        super.process(chunks);
    }
}
