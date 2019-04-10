package jandcode.jc;

import groovy.lang.*;
import jandcode.commons.named.*;

import java.util.*;

/**
 * Команда проекта
 */
public interface Cm extends INamed {

    /**
     * Описание команды
     */
    String getHelp();

    void setHelp(String v);

    /**
     * Опции команды. Можно изменять.
     * Например: <code>cm.get("clean").opts.add(cm.opt("a",false,"Все"))</code>
     */
    NamedList<CmOpt> getOpts();

    /**
     * Определить closure для выполнения команды.
     * Она может иметь 1 параметр {@link CmArgs}, в котором будут переданы
     * аргументы команды.
     */
    void setDoRun(Closure closure);

    /**
     * Выполнить команду с аргументами. Переданный args преобразуется в {@link CmArgs}
     * и заполняется значениями в соответсвии с описанными опциями команды.
     * Можно передавать элементы, которые не описаны в опциях.
     */
    void exec(Map args);

    /**
     * Выполнить команду без аргументов.
     */
    void exec();

}
