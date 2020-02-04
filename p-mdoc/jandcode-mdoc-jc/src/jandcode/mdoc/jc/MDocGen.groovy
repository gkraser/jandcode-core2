package jandcode.mdoc.jc

import jandcode.commons.*
import jandcode.jc.*

/**
 * Генератор исходников для mdoc
 */
class MDocGen extends ProjectScript {

    // список задач
    private List<MDocGenTask> _genTasks = new ArrayList<>()

    protected void onInclude() throws Exception {
        afterLoadAll(this.&onAfterLoadAll)
    }

    void onAfterLoadAll() {
        if (_genTasks.size() > 0) {
            // есть задачи генерации
            cm.add("mdoc-gensrc", this.&cmGenSrc, "Генерация исходников для документации")
        }
    }

    /**
     * Добавить каталог и процесс генерации исходников
     * @param name имя задачи
     * @param destdir куда будем генерировать
     * @param doTask closure с кодом генерации. В качестве параметра принимает
     * ссылку на объект jandcode.mdoc.jc.MDocGenTask
     */
    void genSrc(String name, String destdir, Closure doTask) {
        if (doTask == null) {
            error("Не указана closure для genSrc")
        }
        if (UtString.empty(destdir)) {
            error("Не указан destdir для genSrc")
        }
        MDocGenTask t = new MDocGenTask(name, wd(destdir), doTask)
        _genTasks.add(t)
    }

    /**
     * Команда генерации исходников
     */
    void cmGenSrc() {
        if (_genTasks.size() == 0) {
            return
        }

        // чистим каталоги
        Set<String> outDirs = new HashSet()  // уникальный список
        for (t in _genTasks) {
            outDirs.add(t.dir)
        }
        for (String d in outDirs) {
            ut.cleandir(d)
        }

        // генерация
        for (t in _genTasks) {
            log.info("genSrc to path [${t.dir}]")
            t.exec()
        }
    }

}
