package jandcode.core.web.std.gsp;

import jandcode.commons.variant.*;
import jandcode.core.web.gsp.*;

/**
 * Поддержка для генерации тега jc:page
 */
public interface GspPageManager {

    /**
     * Добавить gsp, как часть страницы.
     * Если deffer=true, то часть сразу отрендерится и буфер будет запомнен.
     * иначе в момент использования будет рендерится тело тега gsp.
     */
    void addPart(String name, BaseGsp gsp, boolean deffer);

    /**
     * Вывести часть страницы
     *
     * @param name какую часть
     */
    void outPart(String name);

    /**
     * Есть ли часть с указанным именем
     */
    boolean hasPart(String name);

    /**
     * Сбросить механизм добавления частей.
     * После вызова новые части будут добавляется в начало списка.
     * Используется для вложенных page
     */
    void resetAddPart();

    /**
     * Сборник аргументов тегов jc:page.
     * Аргументы вложенных тегов имеют низший приоритет.
     */
    IVariantMap getArgs();

}
