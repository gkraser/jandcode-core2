package jandcode.jc.std

import jandcode.commons.*
import jandcode.jc.*

/**
 * Построитель bat-файлов
 */
class BatBuilder extends ProjectScript {

    /**
     * Одна из частей bat-файла
     */
    class Part {
        StringBuilder sb = new StringBuilder()

        void addStr(String s) {
            sb.append("${s}\n")
        }

        void addVar(String name, Object value) {
            String v = UtCnv.toString(value)
            addStr("set ${name}=${v}")
        }

        void addVar(Map vars) {
            for (v in vars) {
                addVar(UtCnv.toString(v.key), v.value)
            }
        }

        void addPath(String path) {
            addVar("PATH", "${path};%PATH%")
        }

    }

    //////

    /**
     * Пролог bat-файла (начало)
     */
    Part prolog = new Part()

    /**
     * Тело bat-файла (середина)
     */
    Part body = new Part()

    /**
     * Эпилог bat-файла (конец)
     */
    Part epilog = new Part()

    protected void onCreate() throws Exception {
        prolog.addStr("@echo off")
    }

    /**
     * Добавить строку в body
     */
    void addStr(String s) {
        body.addStr(s)
    }

    /**
     * Добавить переменную в body
     */
    void addVar(String name, Object value) {
        body.addVar(name, value)
    }

    /**
     * Добавить переменные в body
     */
    void addVar(Map vars) {
        body.addVar(vars)
    }

    /**
     * Добавить путь в epilog
     */
    void addPath(String path) {
        epilog.addPath(path)
    }

    //////

    /**
     * Получить окончательный текст bat-файла
     */
    String buildText() {
        StringBuilder sb = new StringBuilder()
        if (prolog.sb.length() > 0) {
            sb.append(prolog.sb).append("\n")
        }
        if (body.sb.length() > 0) {
            sb.append(body.sb).append("\n")
        }
        if (epilog.sb.length() > 0) {
            sb.append(epilog.sb).append("\n")
        }
        return sb.toString()
    }

    /**
     * Записать bat в указанный файл
     */
    void save(String filename) {
        String s = buildText()
        log "create bat: ${filename}"
        ant.echo(message: s, file: filename)
    }

}
