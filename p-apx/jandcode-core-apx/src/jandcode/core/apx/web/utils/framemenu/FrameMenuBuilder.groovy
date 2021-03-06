package jandcode.core.apx.web.utils.framemenu

import jandcode.commons.*
import jandcode.commons.source.*
import jandcode.core.*
import jandcode.core.web.*
import jandcode.core.web.virtfile.*

import java.util.regex.*

/**
 * Построение меню по папке с файлами vue и js.
 *
 * Если файл vue - то фрейм его export default.
 * Если файл js - если имеется определение заголовка (//#jc title или this.title),
 * то фрейм его export default. Иначе - файл не считается фреймом.
 *
 * В файле берется:
 *
 * this.title='Заголовок' или //#jc title Заголовок
 * this.icon='icon-name' или //#jc icon icon-name
 * Для папки:
 * index.js анализируется на предмет //#jc директив. Папка становится
 * меню с дочерними элементами-фреймами, которые лежат в папке.
 *
 * Игнорируются файлы и папки, которые начинаются с '_'.
 *
 * Если vue-фрем нужно исключить из роутинга и меню (например это диалог),
 * то либо поместите его в папке с '_' вначале, либо:
 * <ul>
 * <li>если в тексте имеется {@code <Dialog>}, то считаем диалогом и игнорируем</li>
 * <li>если в тексте имеется {@code //#jc ignore}, то игнорируем</li>
 * </ul>
 */
class FrameMenuBuilder {

    static Pattern p_title = Pattern.compile("this\\.title\\s*=\\s*[\\'\\\"](.*?)[\\'\\\"]")
    static Pattern p_icon = Pattern.compile("this\\.icon\\s*=\\s*[\\'\\\"](.*?)[\\'\\\"]")

    App app
    FrameMenuItem root
    List<VirtFile> usingFiles = []

    FrameMenuBuilder(App app) {
        this.app = app
        this.root = new FrameMenuItem()
        this.root.folder = true
    }

    void addFolder(String path) {
        WebService webSvc = app.bean(WebService)

        List<VirtFile> files = webSvc.findFiles(path)
        for (VirtFile f : files) {
            addToFolder(root, f, path, "/")
        }
    }

    void addToFolder(FrameMenuItem folder, VirtFile file, String basePath, String routePrefix) {
        if (file.name.startsWith("_")) {
            // игнорируем
            return
        }
        FrameMenuItem item = new FrameMenuItem()
        item.path = file.path
        item.title = UtFile.basename(file.name)

        if (file.isFolder()) {
            WebService webSvc = app.bean(WebService)

            item.folder = true

            VirtFile indexFile = webSvc.findFile(UtVDir.join(file.path, "index.js"))
            if (indexFile != null) {
                fillMetaData(item, indexFile)
            }

            List<VirtFile> files = webSvc.findFiles(file.path)
            for (VirtFile f : files) {
                addToFolder(item, f, basePath, routePrefix)
            }

            if (!item.isEmptyFolder()) {
                folder.addItem(item)
            }

        } else {
            if (!UtVDir.matchPath("**/*.vue", file.path) && !UtVDir.matchPath("**/*.js", file.path)) {
                return
            }
            fillMetaData(item, file)
            usingFiles.add(file)  // по этим файлам потом отслеживаем изменения

            if (item.ignore) {
                return
            }

            folder.addItem(item)

            // make route
            String pt = routePrefix + UtVDir.getRelPath(basePath, file.folderPath)
            String nm = UtString.uncapFirst(UtFile.removeExt(file.name))
            if (!pt.endsWith("/")) {
                pt = pt + "/"
            }
            item.routePath = pt + nm
        }
    }

    void fillMetaData(FrameMenuItem item, VirtFile f) {
        String ext = UtFile.ext(f.getName())

        String text = f.loadText()

        if (text.indexOf("<Dialog") != -1) {
            item.ignore = true
            return
        }

        String title = null
        String icon = null

        Matcher m = p_title.matcher(text)
        if (m.find()) {
            title = m.group(1)
        }
        m = p_icon.matcher(text)
        if (m.find()) {
            icon = m.group(1)
        }

        JcDirectiveExtractor extr = new JcDirectiveExtractor(text)
        for (JcDirective d : extr.getItems()) {
            if (d.hasName("title")) {
                title = d.getParam0()
            } else if (d.hasName("icon")) {
                icon = d.getParam0()
            } else if (d.hasName("ignore")) {
                item.ignore = true
            }
        }

        if (title != null) {
            item.title = title
        }
        if (icon != null) {
            item.icon = icon
        }

        if (title == null && ext == "js") {
            item.ignore = true
        }

        if (UtString.empty(item.title)) {
            item.title = UtFile.removeExt(f.name)
        }

    }

    //////

    /**
     * Конфигурация для routes
     */
    List<Map> getRoutes() {
        List<Map> res = new ArrayList<>()
        internal_getRoutes(res, root)
        return res
    }

    private void internal_getRoutes(List<Map> res, FrameMenuItem root) {
        for (item in root.items) {
            if (!UtString.empty(item.routePath)) {
                Map<String, Object> m = [:]
                m.path = item.routePath
                m.frame = item.path
                res.add(m)
            }
            //
            if (item.isFolder()) {
                internal_getRoutes(res, item)
            }
        }
    }

    /**
     * Конфигурация для меню
     */
    List<Map> getMenu() {
        List<Map> res = new ArrayList<>()
        internal_getMenu(res, root)
        return res
    }

    private void internal_getMenu(List<Map> res, FrameMenuItem root) {
        for (item in root.items) {
            Map<String, Object> m = [:]
            m.label = item.title
            if (item.icon) {
                m.icon = item.icon
            }
            if (item.routePath) {
                m.frame = item.routePath
            }
            res.add(m)
            //
            if (item.isFolder()) {
                List<Map> items = []
                m.items = items
                internal_getMenu(items, item)
            }
        }
    }

}
