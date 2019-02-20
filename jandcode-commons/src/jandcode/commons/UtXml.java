package jandcode.commons;

import jandcode.commons.error.*;
import org.xml.sax.*;

import javax.xml.parsers.*;

/**
 * Утилиты для xml
 */
public class UtXml {

    private static SAXParserFactory simpleSAXParserFactory;

    /**
     * Стандартная фабрика SAXParserFactory без всяких наворотов из мира xml.
     */
    public static SAXParserFactory getSimpleSAXParserFactory() {
        if (simpleSAXParserFactory == null) {
            synchronized (UtXml.class) {
                if (simpleSAXParserFactory == null) {
                    SAXParserFactory tmp = SAXParserFactory.newInstance();
                    try {
                        tmp.setFeature("http://xml.org/sax/features/validation", false); //NON-NLS
                        tmp.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false); //NON-NLS
                        tmp.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false); //NON-NLS
                        tmp.setFeature("http://xml.org/sax/features/external-general-entities", false); //NON-NLS
                        tmp.setFeature("http://xml.org/sax/features/external-parameter-entities", false); //NON-NLS
                    } catch (Exception e) {
                        throw new XErrorWrap(e);
                    }
                    simpleSAXParserFactory = tmp;
                }
            }
        }
        return simpleSAXParserFactory;
    }

    /**
     * Созданние XMLReader для указанного handler.
     * Особенность: xml воспринимается без всяких наворотов из мира xml.
     */
    public static XMLReader createSimpleXMLReader(ContentHandler handler) throws Exception {
        SAXParserFactory factory = getSimpleSAXParserFactory();
        SAXParser parser = factory.newSAXParser();
        XMLReader xreader = parser.getXMLReader();
        xreader.setProperty("http://xml.org/sax/properties/lexical-handler", handler); //NON-NLS
        xreader.setContentHandler(handler);
        return xreader;
    }

}
