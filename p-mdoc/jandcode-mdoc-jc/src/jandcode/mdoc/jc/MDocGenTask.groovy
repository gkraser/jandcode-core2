package jandcode.mdoc.jc

/**
 * Задача генерации исходников для mdoc
 */
class MDocGenTask {

    /**
     * Имя задачи
     */
    String name

    /**
     * В какой каталог генерировать. Абсолютный путь
     */
    String dir

    private Closure doTask

    MDocGenTask(String name, String dir, Closure doTask) {
        this.name = name
        this.dir = dir
        this.doTask = doTask
    }

    /**
     * Запустить задачу
     */
    void exec() {
        this.doTask(this)
    }

}
