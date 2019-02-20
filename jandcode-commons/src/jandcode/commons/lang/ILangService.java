package jandcode.commons.lang;

import java.text.*;

/**
 * Интерфейс, которым пользуется UtLang для работы
 */
public interface ILangService {

    /**
     * Перевод строки на текущий язык
     *
     * @param s      исходная строка
     * @param params параметры для {@link MessageFormat#format(String, Object...)}
     * @return переведенная строка
     */
    public String t(String s, Object... params);

}
