package jandcode.core.nodejs.jc.impl;

import jandcode.commons.*;
import jandcode.commons.io.*;
import jandcode.commons.named.*;
import jandcode.jc.*;
import jandcode.core.nodejs.jc.*;

import java.io.*;
import java.text.*;
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

    private Ctx ctx;
    private String path;
    private Project ownerProject;

    public NodeJsModuleLoader(Ctx ctx, String path, Project ownerProject) {
        this.ctx = ctx;
        this.path = path;
        this.ownerProject = ownerProject;
    }

    public NodeJsModuleLoader(Ctx ctx, String path) {
        this(ctx, path, null);
    }

    public NamedList<NodeJsModule> load() {
        NamedList<NodeJsModule> res = new DefaultNamedList<>();

        if (this.path.indexOf('*') != -1 || this.path.indexOf('?') != -1) {
            // из маски получаем список, далее работаем с ним индивидуально
            DirScanner<File> ds = UtFile.createDirScanner(this.path);
            ds.setNeedDirs(true);
            ds.exclude("**/node_modules");
            List<File> fs = ds.load();
            for (File f : fs) {
                NodeJsModuleLoader tmpLdr = new NodeJsModuleLoader(ctx, f.getAbsolutePath(), this.ownerProject);
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
                addModule(res, this.path);
            }
            return res;
        }

        // если каталог с package.json, то грузим только его
        String pjf = UtFile.join(this.path, NodeJsConsts.PACKAGE_JSON);
        if (UtFile.exists(pjf)) {
            addModule(res, pjf);
            return res;
        }

        // намного быстрее, чем ant (в 2 раза)
        DirScanner<File> ds = UtFile.createDirScanner(this.path);
        ds.include("*/package.json");
        ds.include("@*/*/package.json");
        ds.exclude("**/node_modules");
        List<File> fs = ds.load();

        for (File f : fs) {
            addModule(res, f.toString());
        }

        return res;
    }

    protected void addModule(NamedList<NodeJsModule> res, String filePackageJson) {
        NodeJsModule mod = new NodeJsModuleImpl(filePackageJson, this.ownerProject);
        if (UtString.empty(mod.getName())) {
            ctx.warn(MessageFormat.format("Файл {0} не содержит name, модуль проигнорирован", filePackageJson));
            return;
        }
        res.add(mod);
    }

}
