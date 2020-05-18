package jandcode.core.jsa.jc.impl;

import jandcode.commons.simxml.*;
import jandcode.core.jsa.jc.*;

import java.util.*;

/**
 * Запись списка библиотек nodejs в xml
 */
public class NodeJsLibXmlSaver {

    public void save(List<NodeJsLib> libs, SimXml root) {
        for (NodeJsLib lib : libs) {
            SimXml x = root.addChild("lib");
            x.setValue("name", lib.getName());
            x.setValue("version", lib.getVersion());
            if (lib.isClient()) {
                //
                for (String s : lib.getIncludeClient()) {
                    SimXml x1 = x.addChild("include-client");
                    x1.setValue("mask", s);
                }
                //
                for (String s : lib.getExcludeClient()) {
                    SimXml x1 = x.addChild("exclude-client");
                    x1.setValue("mask", s);
                }
                //
                for (Map.Entry<String, String> en : lib.getModuleMapping().entrySet()) {
                    SimXml x1 = x.addChild("module-mapping");
                    x1.setValue("module", en.getKey());
                    x1.setValue("real-module", en.getValue());
                }
                //
                for (String s : lib.getExcludeRequire()) {
                    SimXml x1 = x.addChild("exclude-require");
                    x1.setValue("mask", s);
                }
            }
        }
    }

}
