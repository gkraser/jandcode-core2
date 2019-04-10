package jandcode.jc.std.idea


import jandcode.jc.*
import jandcode.jc.impl.depends.*
import jandcode.jc.std.*

/**
 * Используется для автоматического включения в проекты для генерации
 * модулей idea для проектов, которые не имеют встроенной поддержки gen-idea
 */
class DummyImlProject extends ProjectScript implements ILibDepends {

    /**
     * каталоги, которые исключаются из модуля и не видны в idea.
     */
    List<String> dirsExclude = ["temp", "out"]

    /**
     * Зависимости
     */
    LibDepends depends


    protected void onInclude() throws Exception {

        this.depends = new LibDependsImpl(ctx, this)
        // По умолчанию включен jandcode-jc для поддержки jc и groovy.
        this.depends.dev.add("jandcode-jc")

        ClasspathUsed cp = getIncluded(ClasspathUsed)
        if (cp != null) {
            // в процессе загрузки были вызовы classpath, включаем все в dev
            this.depends.dev.add(cp.usedClasspath)
        }

        include(GenIdea)
        onEvent(GenIdea.Event_GenIml, this.&genImlHandler)
        cm.add("gen-idea", this.&cmGenIdea, "Генерация модуля idea")
    }

    void cmGenIdea(CmArgs args) {
        GenIdea g = include(GenIdea)
        g.genIml()
    }

    void genImlHandler(GenIdea.Event_GenIml e) {
        ImlXml x = e.x
        //
        x.removeFolders()
        x.addFolders(dirsExclude, "exclude")
        //
        x.removeDepends()

        LibDepends deps = create(LibDependsUtils).getDepends(project)
        ListLib rlibs = deps.all.libsAll
        x.addDepends(rlibs)
    }

}
