package blackop778.steamCommonGames;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

public abstract class XMLParser {
    public static void parse2XML(File xml1, File xml2, File... xmls) {
	try {
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader reader = factory.createXMLEventReader(xml1.getAbsolutePath(), new FileInputStream(xml1));
		while(reader.hasNext()) {
		    XMLEvent event = reader.nextEvent();
		    System.out.println(event.toString());
		}
		}
		catch(FileNotFoundException e) {
		    e.printStackTrace();
		}
		catch(XMLStreamException ex) {
		    ex.printStackTrace();
		}
    }
    
    public static String readFile(String path, Charset encoding) 
	    throws IOException 
	  {
	    byte[] encoded = Files.readAllBytes(Paths.get(path));
	    return new String(encoded, encoding);
	  }
}
