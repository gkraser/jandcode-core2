package jandcode.jc.std

import jandcode.commons.*
import jandcode.jc.*

/**
 * Объект хранит classpath (имена библиотек), которые были использованы для проекта.
 * Используется для ослеживания добавления библиотек в classpath при загрузке проекта.
 * При вызове classpath() для проекта этот скрипт автоматически включается.
 */
class ClasspathUsed extends ProjectScript {

    /**
     * Список использованных classpath для этого проекта
     */
    Set<String> usedClasspath = new LinkedHashSet<>()

    /**
     * Добавить библиотеки в classpath
     * @param libs
     */
    void addClasspathUsed(Object libs) {
        List<String> tmp = UtCnv.toNameList(libs)
        usedClasspath.addAll(tmp)
    }

}
