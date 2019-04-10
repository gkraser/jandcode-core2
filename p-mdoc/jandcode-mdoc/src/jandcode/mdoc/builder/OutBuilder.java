package jandcode.mdoc.builder;

import jandcode.core.*;
import jandcode.mdoc.*;
import jandcode.mdoc.gsp.*;
import jandcode.mdoc.topic.*;

/**
 * Построитель документации.
 */
public interface OutBuilder extends IAppLink, BeanFactoryOwner {

    /**
     * Запустить процесс сборки.
     * Собираются все внутренние структуры (outFiles), ничего никуда не пишется.
     */
    void build() throws Exception;

    /**
     * Документ, который строится.
     * Это не оригинальный документ, это обертка вокруг оригинального документа.
     * Имеет собственный набор topics и sourceFiles, которые могут доформировываться
     * в процессе генерации.
     */
    Doc getDoc();

    /**
     * Оригинальный документ, по которому строим.
     */
    Doc getOriginalDoc();

    /**
     * Формируемые файлы
     */
    OutFileHolder getOutFiles();

    /**
     * Содержание документа.
     */
    Toc getToc();

    /**
     * Распознователь ссылок
     */
    RefResolver getRefResolver();

    /**
     * Режимы работы (debug, serve...).
     * Ссылка на doc.getMode()
     */
    DocMode getMode();

    /**
     * Вывести все в указанный каталог
     *
     * @param outDir куда выводить. Каталог не очищается!
     */
    void outTo(OutDir outDir) throws Exception;

    /**
     * Создать контекст генерации gsp.
     *
     * @param outFile для какого файла
     */
    GspTemplateContext createGspTemplateContext(OutFile outFile);

    /**
     * Есть ли изменения в зависимых файлах с указанного времени
     *
     * @param outFile для какого файла
     */
    boolean hasChangedSinceTime(OutFile outFile, long time);

}