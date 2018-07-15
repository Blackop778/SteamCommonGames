package blackop778.steamCommonGames;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class XMLParser {
    static Game[] findCommonGames(CommonGamesWorker worker, URL xml1, URL xml2, URL... xmls) throws InterruptedException, ReaderCancelledException, ExecutionException, XMLStreamException {
        ArrayList<URLReader> urlReaders = new ArrayList<>();
        urlReaders.add(new URLReader(xml1));
        urlReaders.add(new URLReader(xml2));
        for (URL url : xmls) {
            urlReaders.add(new URLReader(url));
        }

        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 4, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(urlReaders.size()));
        List<Future<String>> futures = executor.invokeAll(urlReaders);
        ArrayList<String> results = new ArrayList<>();
        for (Future<String> result : futures) {
            if (result.isCancelled())
                throw new ReaderCancelledException();

            results.add(result.get());
        }
        results.sort(new LengthComparator());

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader reader = factory.createXMLEventReader(new StringReader(results.get(0)));
        results.remove(0);

        boolean lastAppID = false;
        boolean findAppName = false;
        boolean lastAppName = false;
        String appID = "";
        ArrayList<Game> games = new ArrayList<>();

        outerLoop: while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (lastAppID) {
                lastAppID = false;
                appID = ((Characters) event).getData();
                for(String gamesList : results) {
                    if(!gamesList.contains(appID)) {
                        continue outerLoop;
                    }
                }

                findAppName = true;
            } else if (lastAppName) {
                lastAppName = false;
                String name = ((Characters) event).getData();

                Game game = new Game(appID, name);

                if (worker != null) {
                    worker.publishGame(game);
                }
                games.add(game);
            } else if (event.isStartElement()) {
                StartElement start = (StartElement) event;
                String name = start.getName().getLocalPart();

                if (name.equalsIgnoreCase("appid")) {
                    lastAppID = true;
                } else if (findAppName && name.equalsIgnoreCase("name")) {
                    lastAppName = true;
                    findAppName = false;
                }
            }
        }

        return games.toArray(new Game[]{});
    }

    private static class URLReader implements Callable<String> {
        private final URL url;

        URLReader(URL url) {

            this.url = url;
        }

        @Override
        public String call() throws Exception {
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            StringBuilder contents = new StringBuilder();

            while ((line = br.readLine()) != null) {
                contents.append(line);
            }

            return contents.toString();
        }
    }

    public static class ReaderCancelledException extends Exception {

    }

    private static class LengthComparator implements Comparator<CharSequence> {

        @Override
        public int compare(CharSequence o1, CharSequence o2) {
            return Integer.compare(o1.length(), o2.length());
        }

        @Override
        public boolean equals(Object obj) {
            return equals(obj);
        }
    }

    public static class Game {
        public final String appID;
        public final String name;

        Game(String appID, String name) {
            this.appID = appID;
            this.name = name;
        }
    }
}
