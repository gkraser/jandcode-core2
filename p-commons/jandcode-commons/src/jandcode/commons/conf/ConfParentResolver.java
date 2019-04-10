package jandcode.commons.conf;

/**
 * Рапознаватель особых parent
 */
public interface ConfParentResolver {

    /**
     * Метод берет значение атрибута parent и превращает его в путь до
     * реального объекта.
     *
     * @param type   для какого типа
     * @param parent значение атрибута parent
     * @return null, если parent неизвестен
     */
    String resolveParent(String type, String parent);

}
