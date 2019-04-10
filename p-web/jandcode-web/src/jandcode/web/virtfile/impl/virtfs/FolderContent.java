package jandcode.web.virtfile.impl.virtfs;

import jandcode.web.virtfile.*;

import java.util.*;

/**
 * Папка и ее содержимое с точки зрения точки монтирования.
 * Неизменяемый объект.
 */
public interface FolderContent {

    /**
     * Виртуальный путь папки
     */
    String getPath();

    /**
     * Содержимое папки
     */
    List<VirtFile> getFiles();

    /**
     * Правильная ли папка. Если false - то такая папка в принципе не может
     * существовать. Например для точки монтирования указан путь,
     * который находится вне пути монтирования
     */
    boolean isValid();

    /**
     * Существует ли эта папка.
     */
    boolean isExists();

    /**
     * Может ли содержимое папки изменится
     */
    boolean isCanChange();

    /**
     * Изменилось лм содержимое папки после загрузки
     */
    boolean isChange();
}
