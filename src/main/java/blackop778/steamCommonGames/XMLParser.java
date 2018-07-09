package blackop778.steamCommonGames;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public abstract class XMLParser {
    public static Game[] FindCommonGames(URL xml1, URL xml2, URL... xmls) {
        return FindCommonGames(null, xml1, xml2, xmls);
    }

    public static Game[] FindCommonGames(CommonGamesWorker worker, URL xml1, URL xml2, URL... xmls) {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader reader = factory.createXMLEventReader(new InputStreamReader(xml1.openStream()));
            boolean lastAppID = false;
            boolean findAppName = false;
            boolean lastAppName = false;
            int appID = 0;
            ArrayList<Game> games = new ArrayList<>();
            String file2 = readURL(xml2);
            String[] others = new String[xmls.length];
            for (int i = 0; i < others.length; i++) {
                others[i] = readURL(xmls[i]);
            }
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                if (lastAppID) {
                    lastAppID = false;
                    Characters chars = (Characters) event;
                    if (file2.contains(chars.getData())) {
                        boolean all = true;
                        for (String other : others) {
                            if (!other.contains(chars.getData())) {
                                all = false;
                                break;
                            }
                        }
                        if (all) {
                            findAppName = true;
                            appID = Integer.valueOf(chars.getData());
                        }
                    }
                } else if (lastAppName) {
                    lastAppName = false;
                    String name = ((Characters) event).getData();
                    System.out.println("Name: " + name);
                    if(name.contains("<![CDATA[[")) {
                        name = name.split("\\[")[2];
                        name = name.split("]")[0];
                        name = name.substring(1, name.length() - 1);
                    }
                    Game game = new Game(appID, name);
                    if(worker != null) {
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
        } catch (XMLStreamException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String readURL(URL url) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        StringBuilder contents = new StringBuilder();
        while ((line = br.readLine()) != null) {
            contents.append(line);
        }
        return contents.toString();
    }

    public static class Game {
        public final int appID;
        public final String name;

        Game(int appID, String name) {
            this.appID = appID;
            this.name = name;
        }
    }
}
