package jandcode.jc.std.idea

import jandcode.commons.*
import jandcode.commons.simxml.*
import jandcode.jc.*

/**
 * Класс для iml представления xml
 */
class ImlXml extends BaseXml {

    ImlXml(Project project, SimXml root) {
        super(project, root)
    }

    /**
     * Информация о модуле
     */
    SimXml getRootModule() {
        return root.findChild("component@name=NewModuleRootManager", true)
    }

    /**
     * Информация о каталогах модуля
     */
    SimXml getRootContent() {
        return rootModule.findChild("content", true)
    }

    /**
     * Удалить все зависимости модуля
     */
    void removeDepends() {
        SimXml xc = rootModule

        List<SimXml> forDelete = []
        for (SimXml x1 : xc.getChilds()) {
            if (x1.name == "orderEntry" && (x1["type"] == "module-library" || x1["type"] == "module")) {
                forDelete.add(x1)
            }
        }

        for (SimXml x1 : forDelete) {
            xc.removeChild(x1)
        }
    }

    /**
     * Добавить зависимость от библиотеки. Если она в исходниках, добавляется
     * зависимость от модуля.
     *
     * @return добавленный xml узел
     */
    SimXml addDepend(Lib lib) {
        SimXml xc = rootModule
        def x1 = xc.addChild("orderEntry")
        if (lib.sourceProject != null) {
            x1["type"] = "module"
            x1["module-name"] = lib.name
        } else {
            x1["type"] = "module-library"
            def x2 = x1.addChild("library")
            x2["name"] = lib.name
            def x3, x4

            if (!UtString.empty(lib.jar)) {
                x3 = x2.addChild("CLASSES")
                x4 = x3.addChild('root')
                x4["url"] = "jar://${lib.jar}!/"
            }

            if (!UtString.empty(lib.src)) {
                x3 = x2.addChild("SOURCES")
                x4 = x3.addChild('root')
                x4["url"] = "jar://${lib.src}!/"
            }
        }
        return x1
    }

    /**
     * Добавление зависимостей
     */
    void addDepends(ListLib libs) {
        for (lib in libs) {
            addDepend(lib)
        }
    }

    /**
     * Удалить все зависимости модуля
     */
    void removeFolders() {
        SimXml xc = rootContent
        xc.clearChilds()
    }

    /**
     * Добавить папку
     * @param dir папка, относительно корня модуля
     * @param type тип папки: src,test,exclude,src.gen
     * @return добавленный xml узел
     */
    SimXml addFolder(String dir, String type) {
        SimXml x = rootContent
        def rdir = UtFile.getRelativePath(project.wd.path, project.wd(dir));
        def udir = 'file://$MODULE_DIR$/' + rdir
        def nname = 'sourceFolder'
        if ("exclude" == type) {
            nname = 'excludeFolder'
        }
        SimXml x1 = x.addChild(nname)
        x1.attrs['url'] = udir
        if ("test" == type || "test.gen" == type) {
            x1.attrs['isTestSource'] = true
        }
        if (type.endsWith(".gen")) {
            x1.attrs['generated'] = true
        }
        return x
    }

    /**
     * Добавить папки
     * @param dirs список папок
     * @param type тип папки: src,test,exclude
     * @return добавленный xml узел
     */
    void addFolders(List dirs, String type) {
        for (dir in dirs) {
            addFolder(dir, type)
        }
    }

}
