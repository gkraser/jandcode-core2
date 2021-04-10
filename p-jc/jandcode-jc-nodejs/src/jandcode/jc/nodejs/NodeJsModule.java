package jandcode.jc.nodejs;

import jandcode.commons.named.*;
import jandcode.jc.*;

import java.util.*;

/**
 * Описание nodejs модуля
 */
public interface NodeJsModule extends INamed {

    /**
     * Владелец модуля. Проект, в рамках которого подключен этот модуль.
     * Если значение null, то этот модуль - поставляемая библиотека.
     */
    Project getOwnerProject();

    /**
     * Имя модуля, как его знает npm. Например '@jandcode/module1' или 'quasar'.
     */
    String getName();

    /**
     * Версия
     */
    String getVersion();

    /**
     * Путь до корня модуля, где лежит package.json
     */
    String getPath();

    /**
     * Содержимое package.json
     */
    Map<String, Object> getPackageJson();

    /**
     * Зависимости. Секция dependencies в package.json
     */
    Map<String, String> getDependencies();

    /**
     * Зависимости. Секция devDependencies в package.json
     */
    Map<String, String> getDevDependencies();

    /**
     * Все зависимости из всех секций dependencies в package.json
     */
    Map<String, String> getAllDependencies();

}
