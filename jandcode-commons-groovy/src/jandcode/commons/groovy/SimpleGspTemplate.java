package jandcode.commons.groovy;

import java.util.*;

/**
 * Простой шаблон для генерации текста.
 * Можно использовать напрямую или как прототип для своих шаблонов.
 */
public abstract class SimpleGspTemplate implements IGspTemplate {

    protected StringBuilder sb = new StringBuilder();
    protected Object th;
    protected Map<String, Object> args = new HashMap<>();

    /**
     * Сгенерировать текст для указаного объекта и вернуть сгенерированный результат.
     */
    public String generate(Object th) throws Exception {
        this.sb.setLength(0);
        this.th = th;
        //
        doGenerate();
        //
        return sb.toString();
    }

    //////

    /**
     * Метод, чьем телом будет gsp.
     */
    protected abstract void doGenerate() throws Exception;

    //////

    public void out(Object s) throws Exception {
        sb.append(s);
    }

    /**
     * Ссылка на объект, для которого генерируем
     */
    public Object getTh() {
        return th;
    }

    /**
     * Произвольные аргументы
     */
    public Map<String, Object> getArgs() {
        return args;
    }

}
