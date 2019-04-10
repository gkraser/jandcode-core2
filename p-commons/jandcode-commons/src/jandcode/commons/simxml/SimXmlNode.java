package jandcode.commons.simxml;

import jandcode.commons.simxml.impl.*;

/**
 * Реализация {@link SimXml} по умолчанию
 */
public class SimXmlNode extends SimXmlImpl {

    public SimXmlNode() {
    }

    public SimXmlNode(String name) {
        setName(name);
    }

}
