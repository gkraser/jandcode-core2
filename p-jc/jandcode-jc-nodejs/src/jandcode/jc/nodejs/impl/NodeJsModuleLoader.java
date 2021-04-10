package jandcode.jc.nodejs.impl;

import jandcode.commons.*;
import jandcode.commons.io.*;
import jandcode.commons.named.*;
import jandcode.jc.*;
import jandcode.jc.nodejs.*;

import java.io.*;
import java.util.*;

/**
 * Загрузчик модулей из указанного пути.
 * Если путь является маской, то грузит все файлы/каталоги по указанной маске и
 * индивидуально их обрабатывает по нижеуказанным правилам.
 * <p>
 * Если путь указывает на файл package.json, то грузим этот модуль.
 * Если путь указывает на каталог с файлом package.json, то грузим этот модуль.
 * Иначе считается что папка в формате node_modules и загружается все, что в ней есть.
 * Т.е. папки верхнего уровня и папки '@name1/name2' второго уровня.
 */
public class NodeJsModuleLoader extends ProjectScript {

    private String path;
    private Project ownerProject;

    public NodeJsModuleLoader(String path, Project ownerProject) {
        this.path = path;
        this.ownerProject = ownerProject;
    }

    public NodeJsModuleLoader(String path) {
        this(path, null);
    }

    public NamedList<NodeJsModule> load() {
        NamedList<NodeJsModule> res = new DefaultNamedList<>();

        if (this.path.indexOf('*') != -1 || this.path.indexOf('?') != -1) {
            // из маски получаем список, далее работаем с ним индивидуально
            DirScanner<File> ds = UtFile.createDirScanner(this.path);
            ds.setNeedDirs(true);
            List<File> fs = ds.load();
            for (File f : fs) {
                NodeJsModuleLoader tmpLdr = new NodeJsModuleLoader(f.getAbsolutePath(), this.ownerProject);
                NamedList<NodeJsModule> tmpLst = tmpLdr.load();
                res.addAll(tmpLst);
            }
            return res;
        }

        if (!UtFile.exists(this.path)) {
            return res;
        }

        if (UtFile.isFile(this.path)) {
            // если файл, то он должен иметь имя package.json
            String fn = UtFile.filename(this.path);
            if (NodeJsConsts.PACKAGE_JSON.equals(fn)) {
                NodeJsModule m = new NodeJsModuleImpl(this.path, this.ownerProject);
                res.add(m);
            }
            return res;
        }

        // если каталог с package.json, то грузим только его
        String pjf = UtFile.join(this.path, NodeJsConsts.PACKAGE_JSON);
        if (UtFile.exists(pjf)) {
            NodeJsModule m = new NodeJsModuleImpl(pjf, this.ownerProject);
            res.add(m);
            return res;
        }

        // намного быстрее, чем ant (в 2 раза)
        DirScanner<File> ds = UtFile.createDirScanner(this.path);
        ds.include("*/package.json");
        ds.include("@*/*/package.json");
        ds.exclude("**/node_modules");
        List<File> fs = ds.load();

        for (File f : fs) {
            NodeJsModule mod = new NodeJsModuleImpl(f.toString(), this.ownerProject);
            res.add(mod);
        }

        return res;
    }

}
