package jandcode.core.dbm.verdb;

import jandcode.commons.named.*;
import jandcode.core.dbm.*;

import java.util.*;

/**
 * Модуль verdb.
 * <p>
 * Имеет имя модуля и ссылку на каталог с ресурсами changeset для этого модуля.
 * В модели в принципе может быть несколько таких модулей.
 * Каждый модуль отвечает за некоторое
 * самостоятельное подножество объектов базы данных.
 * <p>
 * Имя самого объекта - это просто имя из конфигурации. Обычно 'default'.
 * <p>
 * По умолчанию модулей нет.
 * <p>
 * Используется конфигурация 'verdb-module'.
 */
public interface VerdbModule extends INamed, IModelMember, IVerdbModuleDef {

    /**
     * Каталоги в модуле.
     */
    List<VerdbDir> getDirs();

    /**
     * Для базы данных версии curVersion возвращает набор операторов, необходимый, что бы
     * довести ее до версии lastVersion.
     *
     * @param curVersion  текущая версия
     * @param lastVersion до какой версии довести. Может быть null, тогда до последней.
     *                    Может быть не существующей версией, тогда ворзращаютя все,
     *                    которые меньше или равны ей.
     * @return набор операторов в правильном порядке. Список может быть пустой.
     */
    List<VerdbOper> getOpers(VerdbVersion curVersion, VerdbVersion lastVersion);

}
