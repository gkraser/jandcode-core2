package jandcode.core.jsa.jc.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.simxml.*;
import jandcode.core.jsa.jc.*;

/**
 * Загрузчик списка описаний библиотек nodejs из xml-данных.
 */
public class NodeJsLibXmlLoader {

    public NodeJsLibList load(SimXml root) {
        NodeJsLibList res = new NodeJsLibList();

        for (SimXml x : root.getChilds()) {
            if (x.hasName("lib")) {
                NodeJsLib lib = loadLib(x);
                res.add(lib);
            } else {
                throw new XError("Неизвестный узел {0}", x.getName());
            }
        }

        return res;
    }

    private NodeJsLibImpl loadLib(SimXml root) {
        String name = root.getString("name");
        String version = root.getString("version");
        boolean client = root.getBoolean("client");
        //

        if (UtString.empty(name)) {
            throw new XError("Атрибут name не определен");
        }
        if (UtString.empty(version)) {
            throw new XError("Атрибут version не определен");
        }

        NodeJsLibImpl res = new NodeJsLibImpl();
        res.setName(name);
        res.setVersion(version);
        res.setClient(client);

        for (SimXml x : root.getChilds()) {
            if (x.hasName("exclude-client")) {
                String mask = x.getString("mask");
                if (UtString.empty(mask)) {
                    throw new XError("Атрибут mask не определен в exclude-client для lib {0}", name);
                }
                res.getExcludeClient().add(mask);

            } else if (x.hasName("include-client")) {
                String mask = x.getString("mask");
                if (UtString.empty(mask)) {
                    throw new XError("Атрибут mask не определен в include-client для lib {0}", name);
                }
                res.getIncludeClient().add(mask);

            } else if (x.hasName("module-mapping")) {
                String module = x.getString("module");
                String realModule = x.getString("real-module");
                if (UtString.empty(module)) {
                    throw new XError("Атрибут module не определен в module-mapping для lib {0}", name);
                }
                if (UtString.empty(realModule)) {
                    throw new XError("Атрибут real-module не определен в module-mapping для lib {0}", name);
                }
                res.getModuleMapping().put(module, realModule);

            } else if (x.hasName("exclude-require")) {
                String mask = x.getString("mask");
                if (UtString.empty(mask)) {
                    throw new XError("Атрибут mask не определен в exclude-require для lib {0}", name);
                }
                res.getExcludeRequire().add(mask);

            } else {
                throw new XError("Неизвестный узел {0}", x.getName());
            }
        }

        return res;
    }

}
