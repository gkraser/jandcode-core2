package jandcode.mdoc

import jandcode.commons.*
import jandcode.commons.conf.*
import jandcode.core.*
import jandcode.core.test.*
import jandcode.mdoc.builder.*
import jandcode.mdoc.cfg.*
import jandcode.mdoc.source.*
import jandcode.mdoc.topic.*

class MDocTestSvc extends BaseAppTestSvc {

    /**
     * MDocService
     */
    MDocService getSvc() {
        return getApp().bean(MDocService.class);
    }

    /**
     * Получить незагруженный документ по имени из appConf:test/mdoc/doc/NAME
     */
    Doc getDoc(String name) throws Exception {
        Conf conf = app.conf.getConf("test/mdoc/doc/${name}")
        DocCfg cfg = svc.docCfgService.createDocCfg(conf)
        cfg.props['_docName'] = name  // имя сохраняем
        return svc.createDocument(cfg)
    }

    /**
     * Загрузить документ по имени из appConf:test/mdoc/doc/NAME
     */
    Doc loadDoc(String name) throws Exception {
        Doc doc = getDoc(name);
        doc.load();
        return doc;
    }

    /**
     * Загрузить документ по имени из appConf:test/mdoc/doc/NAME,
     * создатьи построить для него builder
     */
    OutBuilder buildDoc(String name) {
        Doc doc = loadDoc(name)
        OutBuilder builder = doc.createBuilder("html")
        builder.build()
        return builder
    }

    /**
     * Путь в каталоге test-data
     */
    String getTestDataPath(String subPath = "") {
        ModuleInst mod = app.getModules().get("jandcode.mdoc");
        String path = UtFile.join(mod.getSourceInfo().getProjectPath(), "test-data");
        if (!UtString.empty(subPath)) {
            path = UtFile.join(path, subPath)
        }
        return path;
    }

    ////// print

    /**
     * Содержание в строку для просмотра и печати
     */
    String tocToStr(Toc toc) {
        StringBuilder sb = new StringBuilder()
        tocToStr_internal(sb, toc, "")
        return UtString.trimLast(sb.toString())
    }

    /**
     * Напечатать содержание
     */
    void printToc(Toc toc) {
        println tocToStr(toc)
    }

    private void tocToStr_internal(StringBuilder sb, Toc toc, String indent) {
        String topicId = toc.topic == null ? "(NOTOPIC)" : toc.topic.id
        sb.append("${indent}${topicId} : ${toc.title}\n")
        for (Toc ch : toc.childs) {
            tocToStr_internal(sb, ch, indent + "- ")
        }
    }

    /**
     * Напечатать документ
     */
    void printDoc(Doc doc) {
        utils.delim("props");
        for (p in doc.cfg.props) {
            println "${p.key} = ${p.value}"
        }

        utils.delim("files");
        for (SourceFile f : doc.getSourceFiles()) {
            String rp = f.getRealPath()
            if (rp != null) {
                rp = " => ${rp}"
            } else {
                rp = " => <NULL>"
            }
            println "${f.path}${rp}"
        }

        utils.delim("topics");
        for (Topic t : doc.getTopics()) {
            println t.getId()
        }
    }

}
