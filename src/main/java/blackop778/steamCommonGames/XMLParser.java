package blackop778.steamCommonGames;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public abstract class XMLParser {
    public static void parse2XML(File xml1, File xml2, File... xmls) {
	try {
	    XMLInputFactory factory = XMLInputFactory.newInstance();
	    XMLEventReader reader = factory.createXMLEventReader(xml1.getAbsolutePath(), new FileInputStream(xml1));
	    boolean lastAppID = false;
	    boolean lastSteamID = false;
	    ArrayList<Game> games = new ArrayList<>();
	    String file2 = readFile(xml2.getAbsolutePath(), Charset.availableCharsets().get("UTF-8"));
	    String[] others = new String[xmls.length];
	    for (int i = 0; i < others.length; i++) {
		others[i] = readFile(xmls[i].getAbsolutePath(), Charset.availableCharsets().get("UTF-8"));
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

			}
		    }
		}
		if (event.isStartElement()) {
		    StartElement start = (StartElement) event;
		    String name = start.getName().getLocalPart();
		    if (name.equalsIgnoreCase("appid")) {
			lastAppID = true;
		    } else if (name.equalsIgnoreCase("steamid")) {
			lastSteamID = true;
		    }
		}
	    }
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (XMLStreamException ex) {
	    ex.printStackTrace();
	} catch (IOException exc) {
	    exc.printStackTrace();
	}
    }

    public static String readFile(String path, Charset encoding) throws IOException {
	byte[] encoded = Files.readAllBytes(Paths.get(path));
	return new String(encoded, encoding);
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
