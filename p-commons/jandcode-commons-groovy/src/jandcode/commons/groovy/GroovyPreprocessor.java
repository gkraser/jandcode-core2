package jandcode.commons.groovy;

/**
 * Препроцессор для текста скрипта.
 * Назначается компилятору.
 */
public interface GroovyPreprocessor {

    /**
     * Обработать текст скрипта перед превращаением его в класс
     *
     * @param text      текст скрипта
     * @param className имя класса
     * @param baseClass базовый класс
     * @param sign      сигнатура
     * @return текст скрипта
     */
    String preprocessScriptText(String text, String className, Class baseClass, String sign);

}
