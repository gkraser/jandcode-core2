package jandcode.commons.str;

/**
 * Интерфейс дедубликатора строк
 */
public interface StrDedup {

    /**
     * Вернуть строку, соответсвующую s, но без дублирования экземпляра.
     *
     * @param s оригинальная строка
     * @return дедублицированная строка
     */
    String dedup(String s);

    /**
     * Очистить внутренний кеш.
     */
    void clear();

}
