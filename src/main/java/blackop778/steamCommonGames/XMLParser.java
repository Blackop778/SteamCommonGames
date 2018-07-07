package blackop778.steamCommonGames;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public abstract class XMLParser {
    public static ArrayList<Game> parse2XML(URL xml1, URL xml2, URL... xmls) {
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
                        for (int i = 0; all & i < others.length; i++) {
                            if (!others[i].contains(chars.getData())) {
                                all = false;
                            }
                        }
                        if (all) {
                            findAppName = true;
                            appID = Integer.valueOf(chars.getData());
                        }
                    }
                } else if (lastAppName) {
                    lastAppName = false;
                    String data = ((Characters) event).getData().split("[")[2];
                    data = data.split("]")[0];
                    data = data.substring(1, data.length() - 1);
                    games.add(new Game(appID, data));

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
                return games;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XMLStreamException ex) {
            ex.printStackTrace();
        } catch (IOException exc) {
            exc.printStackTrace();
        }

        return null;
    }

    public static String readURL(URL url) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        String contents = "";
        while ((line = br.readLine()) != null) {
            contents += line;
        }
        return contents;
    }

    public static class Game {
        public final int appID;
        public final String name;

        public Game(int appID, String name) {
            this.appID = appID;
            this.name = name;
        }
    }
}
