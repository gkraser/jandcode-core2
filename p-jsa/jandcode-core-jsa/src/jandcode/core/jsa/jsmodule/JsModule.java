package jandcode.core.jsa.jsmodule;

import jandcode.core.*;
import jandcode.core.web.virtfile.*;

import java.util.*;

/**
 * Модуль js
 */
public interface JsModule extends Comp, IModuleText {

    /**
     * Имя модуля. Полное имя файла в virtFs
     */
    String getName();

    /**
     * Файл, которым представлен модуль
     */
    VirtFile getFile();

    /**
     * id в виде короткой строки. Уникально для каждого модуля.
     */
    String getId();

    /**
     * Описанные requires в этом модуле
     */
    List<RequireItem> getRequires();

    /**
     * Раскрытые зависимости модуля
     */
    List<JsModule> getRequiresExpanded();

    /**
     * true, если был изменен после создания экземпляра.
     * В этом случае требуется пересоздание
     */
    boolean isModified();

    /**
     * Динамический модуль.
     * Динамические модули возвращают каждый раз новый текст при ображению к getText()
     */
    boolean isDynamic();

}
