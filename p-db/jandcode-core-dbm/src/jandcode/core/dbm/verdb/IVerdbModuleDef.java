package jandcode.core.dbm.verdb;

import jandcode.commons.named.*;
import jandcode.core.*;

/**
 * Свойства verdb-модуля
 */
public interface IVerdbModuleDef extends INamed {

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
