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
     * Имя темы по умолчанию (std).
     */
    String getDefaultThemeName();

    /**
     * Имя js-файла с темой. Если такой темы нет, возвращается тема по умолчанию.
     */
    String getThemeFile(String name);

}
