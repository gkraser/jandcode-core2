package jandcode.core.dbm;

/**
 * Режим структуры базы данных для модели
 */
public enum DbMode {

    /**
     * Модель не имеет собственной структуры базы данных
     */
    none,

    /**
     * Модель имеет собственную структуру базы данных,
     * которая состоит только из объектов, объявленных в модели.
     */
    module,

    /**
     * Модель имеет объединенную структуру базы данных, которая состоит
     * из наложения всего, что в нее входит.
     */
    solid;

    /**
     * Конвертация из строки
     */
    public static DbMode fromString(String s) {
        if (s == null || s.length() == 0) {
            return none;
        }
        if (module.toString().equals(s)) {
            return module;
        }
        if (solid.toString().equals(s)) {
            return solid;
        }
        return none;
    }

}
