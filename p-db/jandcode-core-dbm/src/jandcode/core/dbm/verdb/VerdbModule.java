package jandcode.core.dbm.verdb;

import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.dbm.*;

/**
 * Модуль verdb.
 * <p>
 * Имеет имя модуля и ссылку на каталог с ресурсами changeset для этого модуля.
 * В модели в принципе может быть несколько таких модулей.
 * Каждый модуль отвечает за некоторое
 * самостоятельное подножество объектов базы данных.
 * <p>
 * Имя самого объекта - это прости имя из конфигурации. Обычно 'default'.
 * <p>
 * По умолчанию модулей нет.
 * <p>
 * Используется конфигурация 'verdb-module'.
 */
public interface VerdbModule extends INamed, IModelMember {

    /**
     * Путь до каталога, в котором содержатся ресурсы changeset.
     * При назначении пути можно использовать префикс 'jc-data:', для хранения
     * ресурсов в каталоге jc-data.
     *
     * @return vfs путь
     * @see UtApp#getFileObject(jandcode.core.App, java.lang.String)
     */
    String getPath();

    /**
     * Имя модуля. Это имя используется как маркер в базе данных.
     * В одной базе данных все имена всех модулей должны быть уникальны.
     * По умолчанию инициализируется именем модели-прототипа.
     */
    String getModuleName();

}
