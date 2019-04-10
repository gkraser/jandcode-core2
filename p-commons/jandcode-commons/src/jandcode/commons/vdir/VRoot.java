package jandcode.commons.vdir;

/**
 * Информация о корне виртуального каталога
 */
public interface VRoot {

    /**
     * Реальный путь до корня
     */
    String getRootPath();

    /**
     * Префикс, с которым каталог примонтирован
     */
    String getPrefix();

}
