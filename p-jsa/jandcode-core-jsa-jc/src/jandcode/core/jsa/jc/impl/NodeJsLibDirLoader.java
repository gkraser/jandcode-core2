package jandcode.core.jsa.jc.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.io.*;
import jandcode.commons.simxml.*;
import jandcode.core.jsa.jc.*;
import jandcode.jc.*;

import java.io.*;
import java.util.*;

/**
 * Загрузка всех nodejs библиотек из указанного каталога
 */
public class NodeJsLibDirLoader {

    private Ctx ctx;
    private String libPath;

    public NodeJsLibDirLoader(Ctx ctx, String libPath) {
        this.ctx = ctx;
        this.libPath = libPath;
    }

    public List<NodeJsLib> load() {
        List<NodeJsLib> res = new NodeJsLibList();

        if (!UtFile.exists(libPath)) {
            return res;
        }

        // формируем список файлов с описаниями библиотек
        DirScanner<File> sc = UtFile.createDirScanner(libPath);
        sc.include("*.nodejs.xml");
        List<File> scLst = sc.load();

        for (File f : scLst) {

            ctx.debug("load nodejs libs from: " + f.getAbsolutePath());

            SimXml x = new SimXmlNode();
            try {
                x.load().fromFile(f);
            } catch (Exception e) {
                throw new XErrorWrap(e);
            }

            NodeJsLibXmlLoader ldr = new NodeJsLibXmlLoader();
            NodeJsLibList lst = ldr.load(x);

            res.addAll(lst);
        }


        return res;
    }

}
