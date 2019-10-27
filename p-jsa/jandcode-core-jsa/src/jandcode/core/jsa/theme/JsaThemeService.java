package jandcode.core.jsa.theme;

import jandcode.core.*;

import java.util.*;

/**
 * Поддержка тем.
 * <p>
 * Темой является файл в модуле с именем 'css/theme-THEMENAME.js'.
 * Его импорт - инициализация темы.
 */
public interface JsaThemeService extends Comp {

    /**
     * Имена доступных тем
     */
    Collection<String> getThemeNames();

    /**
     * Найти js-файл с темой по имени темы.
     * Если такой темы нет, возвращается null.
     */
    String findThemeFile(String name);

}
