package jandcode.mdoc.cfg;

import jandcode.commons.variant.*;

import java.util.*;

/**
 * Конфигурация документа
 */
public class DocCfg {

    private IVariantMap props = new VariantMap();
    private List<FilesetCfg> srcs = new ArrayList<>();

    /**
     * Свойства произвольные
     */
    public IVariantMap getProps() {
        return props;
    }

    /**
     * Набор исходных файлов
     */
    public List<FilesetCfg> getSrcs() {
        return srcs;
    }

    /**
     * Свойство title.
     * Заголовок документации.
     */
    public String getTitle() {
        return getProps().getString("title", "NoTitle");
    }

    /**
     * Имя файла с содержанием. Свойство toc
     */
    public String getToc() {
        return getProps().getString("toc", "");
    }

    ////// утилиты

    /**
     * Установить свойство
     */
    public void setProp(String name, Object value) {
        getProps().put(name, value);
    }

    /**
     * Добавить набор файлов с исходниками
     *
     * @return добавленный набор, можно настраивать свойства
     */
    public FilesetCfg addSrc(FilesetCfg fs) {
        getSrcs().add(fs);
        return fs;
    }

    /**
     * Добавить набор файлов с исходниками
     *
     * @return добавленный набор, можно настраивать свойства
     */
    public FilesetCfg addSrc(String dir) {
        FilesetCfg z = new FilesetCfg();
        getSrcs().add(z);
        z.setDir(dir);
        return z;
    }

}
