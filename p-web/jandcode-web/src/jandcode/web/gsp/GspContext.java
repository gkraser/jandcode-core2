package jandcode.web.gsp;

import jandcode.commons.variant.*;
import jandcode.core.*;
import jandcode.web.virtfile.*;

import java.util.*;

/**
 * Контекст, в рамках которого gsp осуществляет render.
 * Экземпляр создается для каждого процесса рендеринга.
 */
public interface GspContext extends IGspFactory, IVirtFileFind, BeanFactoryOwner, IBeanIniter {

    /**
     * Выводит указанный gsp в буфер конекста и возврaщает этот буфер.
     * Вызов не может быть вложенным.
     *
     * @param gspName имя gsp
     * @param args    аргументы, могут быть null
     */
    ITextBuffer render(String gspName, Map args) throws Exception;

    /**
     * Выводит указанный gsp без аргументов в буфер конекста и возврaщает этот буфер.
     * Вызов не может быть вложенным.
     *
     * @param gspName имя gsp
     */
    ITextBuffer render(String gspName) throws Exception;

    /**
     * Аргументы контекста.
     * Можно рассматривать как глобальные переменные процесса рендеринга.
     */
    IVariantMap getArgs();

    /**
     * Поместить IGspFactory в стек.
     * Теперь она будет участвовать в создании gsp.
     * Если ее метод createGsp возвратит gsp, будет участвовать он,
     * иначе (если возвратит null) - то будут использоваться фабрики, который были до нее.
     */
    void pushGspFactory(IGspFactory factory);

    /**
     * Убрать из стека последнюю фабрику gsp
     */
    void popGspFactory();

    /**
     * Возвращает текущую выводимую gsp.
     */
    BaseGsp getCurrentGsp();

    /**
     * Возвращает корневую gsp, с нее начался вывод
     */
    BaseGsp getRootGsp();

    /**
     * Раскрытие подстановок <code>#{x}</code>.
     *
     * @return строка с подставленными значениями переменных
     */
    String substVar(String s);

    /**
     * Добавить обработчик для {@link GspContext#substVar(java.lang.String)}.
     * Чем позже добавлен, тем раньше используется.
     */
    void addSubstVarHandler(IGspContextSubstVar handler);

}
