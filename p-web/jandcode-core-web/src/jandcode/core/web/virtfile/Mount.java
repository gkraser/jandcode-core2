package jandcode.core.web.virtfile;

import jandcode.core.*;

/**
 * Точка монтирования.
 * Точка монтирования так же может реализовывать интерфейс
 * {@link IMountProvider}, в таком случае она рассматривается
 * как провайдер и монтируется не сама, а то что предоставляет.
 */
public interface Mount extends Comp {

    /**
     * Виртуальный путь, куда монтируется
     */
    String getVirtualPath();

    /**
     * Реальный путь
     */
    String getRealPath();

}
