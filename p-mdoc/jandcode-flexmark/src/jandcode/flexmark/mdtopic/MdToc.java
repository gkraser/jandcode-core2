package jandcode.flexmark.mdtopic;

import java.util.*;

/**
 * Элемент содержания статьи
 */
public interface MdToc {

    /**
     * Заголовок
     */
    String getTitle();

    /**
     * id
     */
    String getId();

    /**
     * Дочерние элементы
     */
    List<MdToc> getChilds();

}
