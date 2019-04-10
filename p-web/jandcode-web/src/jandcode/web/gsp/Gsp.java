package jandcode.web.gsp;

import jandcode.commons.groovy.*;
import jandcode.commons.variant.*;
import jandcode.core.*;

/**
 * Шаблон gsp.
 */
public interface Gsp extends Comp, IGspTemplate, IGspRender {

    /**
     * Имя аргумента, в котором хранится тело
     */
    String ARG_BODY = "body";

    //////

    /**
     * Владелец этого шаблона - кто его вывел
     */
    Gsp getOwner();

    ////// out

    /**
     * Вывести текст. s может быть объектом {@link IGspRender}, тогда предполагается,
     * что он сам себя выведет, иначе - объект преобразуется в текст и выводится.
     */
    void out(Object s);

    ////// buffer

    /**
     * Создать новый буфер и поместить его в стек. Весь вывод будет в новый буфер.
     * Возвращает новый буфер.
     */
    ITextBuffer pushBuffer();

    /**
     * Извлечь последний буфер из стека.
     */
    ITextBuffer popBuffer();

    /**
     * Текущий буфер, в который осуществляется вывод.
     */
    ITextBuffer curBuffer();

    ////// args

    /**
     * Аргументы шаблона, переданные ему при вызове
     */
    IVariantMap getArgs();

    ////// body

    /**
     * Есть ли тело у шаблона
     */
    boolean hasBody();

    /**
     * Вывести тело шаблона, если есть.
     */
    void outBody();

    /**
     * Вывести тело шаблона во временный буфер и вернуть его.
     */
    ITextBuffer grabBody();

    ////// include

    /**
     * Включение шаблона в контексте текущего шаблона. Внутри шаблона gspName
     * args будет такой же, как и у того, откуда включили.
     */
    void include(String gspName) throws Exception;

    /**
     * Включение шаблона в контексте шаблона 'to'. Внутри шаблона gspName
     * args будет такой же, как и у 'to'.
     * Используется для специфических целей, например для реализации include-тегов.
     */
    void include(Gsp to, String gspName) throws Exception;

    ////// context

    /**
     * Контекст, в рамках которого происходит render
     */
    GspContext getContext();

    //////

    /**
     * Установить признак удаления из pool. Если false (по умолчанию), то gsp
     * после render будет возвращен в pool и будет переиспользован при следующем
     * обращении к этой gsp. Если true, то объект будет удален из пула и станет
     * независимым объектом, который можно переиспользовать по ссылке вне
     * контекста его render.
     */
    void setRemoveFromPool(boolean v);

    /**
     * Удалять ли эту gsp из pool объектов gsp. По умолчанию - false.
     * (см. {@link Gsp#setRemoveFromPool(boolean)}
     */
    boolean isRemoveFromPool();

}
