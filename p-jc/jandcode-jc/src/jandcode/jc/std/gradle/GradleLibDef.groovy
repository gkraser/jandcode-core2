package jandcode.jc.std.gradle

/**
 * Описание библиотеки, полученной из gradle
 */
class GradleLibDef {

    /**
     * Имя библиотеки для jc
     */
    String name

    String group

    String module

    String version

    /**
     * Полное имя модуля в gradle
     */
    String displayName

    /**
     * jar, который запишется в Lib.xml.
     * Если не указан, то и не пишется.
     */
    String jar = ""

    /**
     * jar, который предоставляет gradle
     */
    String gradleJar = ""

    /**
     * src, который запишется в lib.xml.
     * Если не указан, то и не пишется.
     */
    String src = ""

    /**
     * src, который предоставляет gradle
     */
    String gradleSrc = ""

    /**
     * Зависимости, определенные в gradle
     */
    List<String> gradleDepends = []

    /**
     * Зависимости, определенные для jc
     */
    Set<String> depends = new HashSet<>()

    /**
     * Эта библиотека нужна, если значение true.
     * Устанавливается в фильтрах, что бы не реализовывать удаление из списка.
     */
    boolean include = true

}
