package jandcode.flexmark.mdtopic;

import com.vladsch.flexmark.util.ast.*;

import java.util.*;

/**
 * Представление markdown документа в виде статьи.
 */
public interface MdTopic {

    /**
     * Распарзенный с помощью flexmark документ md
     */
    Document getDocumentNode();

    /**
     * Тело статьи в виде html
     */
    String getBody();

    /**
     * Заголовок статьи. Может быть null
     */
    String getTitle();

    /**
     * Внутреннее содержание статьи.
     * Возвращаемый элемент является контейнером. Содержание в его childs.
     */
    MdToc getToc();

    /**
     * Свойства, полученные из front matter
     */
    Map<String, String> getProps();

}
