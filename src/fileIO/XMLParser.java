package fileIO;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.InputStream;


public class XMLParser {

    private XMLReader reader;

    public XMLParser(DefaultHandler handler) throws SAXException {
        if (handler != null) {
            reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(handler);
            reader.setErrorHandler(handler);
        } else {
            throw new NullPointerException("Event handler cannot be null");
        }
    }

    public void parse(InputStream in) throws SAXException, IOException{
        reader.parse(new InputSource(in));
    }
}
