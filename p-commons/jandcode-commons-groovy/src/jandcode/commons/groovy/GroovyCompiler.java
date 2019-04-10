package jandcode.commons.groovy;

import org.apache.commons.vfs2.*;

/**
 * Компилятор для groovy-скриптов и шаблонов.
 * <p>
 * Сигнатура в методах (параметр sign):
 * <ul>
 * <li><code>class</code> - текст скрипта - это полное определение класса
 * <li><code>body</code> - текст скрипта - это тело класса, который описан в параметрах
 * <li><code>другое</code> - сигнатура метода, реализацией которого является скрипт
 * </ul>
 * <p>
 * Имена классов генерируются автоматически и не факт, что они читабельны.
 */
public interface GroovyCompiler {

    /**
     * Значение для сигнатуры "полный текст класса"
     */
    String SIGN_CLASS = "class";

    /**
     * Значение для сигнатуры "тело класса"
     */
    String SIGN_BODY = "body";

    /**
     * Каталог, который используется как внешний кеш для скомпилированных скриптов.
     * Если каталог не установлен (по умолчанию), то внешний кеш не используется.
     * <p>
     * При присвоении значения этому свойству, указанный путь автоматически включается в classpath.
     */
    void setCompiledCacheDir(String compiledCacheDir);

    String getCompiledCacheDir();

    /**
     * Получить GroovyClazz для скрипта
     *
     * @param baseClass  базовый класс для скрипта
     * @param sign       сигнатура
     * @param file       файл скрипта
     * @param isTemplate true - рассматривать текст как шаблон
     */
    GroovyClazz getClazz(Class baseClass, String sign,
            FileObject file, boolean isTemplate) throws Exception;


    /**
     * Получить GroovyClazz для скрипта
     *
     * @param baseClass  базовый класс для скрипта
     * @param sign       сигнатура
     * @param text       текст скрипта
     * @param isTemplate true - рассматривать текст как шаблон
     */
    GroovyClazz getClazz(Class baseClass, String sign,
            String text, boolean isTemplate) throws Exception;

    /**
     * Добавить к компилятору препроцессор.
     *
     * @param name         имя. Если препроцессор с таким именем существует, он заменяется
     * @param preprocessor препроцессор
     */
    void addPreprocessor(String name, GroovyPreprocessor preprocessor);

    /**
     * Проверяем изменившиеся файлы
     */
    boolean checkChangedResource();

}
