package jandcode.core.dbm.ddl;

import java.util.*;

/**
 * Стадии формирования ddl для модели.
 */
public enum DDLStage {

    /**
     * Подготовка базы данных для создания объектов модели
     */
    prepareModel,

    /**
     * Создание системных объектов. Например, утилитных хранимых процедур.
     * Таблицы еще не созданы.
     */
    systemObjects,

    /**
     * Перед созданием таблиц.
     */
    beforeTables,

    /**
     * Создание таблиц.
     */
    tables,

    /**
     * Выполняется после создания всех таблиц и view.
     */
    afterTables,

    /**
     * Перед данными, которые являются частью структуры.
     */
    beforeData,

    /**
     * Данные, которые являются частью структуры.
     */
    data,

    /**
     * Выполняется после данных, которые являются частью структуры.
     */
    afterData,

    /**
     * Окончательный этап после создания всего для модели
     */
    postModel;


    /**
     * Конвертация из строки. Для пустых строк возвращает afterTables
     */
    public static DDLStage fromString(String s) {
        if (s == null || s.length() == 0) {
            return afterTables;
        }
        return valueOf(s);
    }

    /**
     * Список всех стадий
     */
    public static List<DDLStage> getStages() {
        return Arrays.asList(DDLStage.values());
    }

}
