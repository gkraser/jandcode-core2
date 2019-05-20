package jandcode.wax.web;

import jandcode.core.*;

import java.util.*;

/**
 * Поддержка тем
 */
public interface WaxThemeService extends Comp {

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
