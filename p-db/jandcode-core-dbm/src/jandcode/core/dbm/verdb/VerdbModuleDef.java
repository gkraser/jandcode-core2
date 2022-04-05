package jandcode.core.dbm.verdb;

import jandcode.core.dbm.*;

/**
 * Определение модуля verdb
 */
public interface VerdbModuleDef extends IVerdbModuleDef, IModelLink {

    /**
     * Создать экземпляр модуля.
     * Модуль сразу згружает каталоги и файлы.
     */
    VerdbModule createInst();

}
