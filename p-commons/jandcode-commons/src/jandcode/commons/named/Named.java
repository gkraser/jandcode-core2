package jandcode.commons.named;

/**
 * Объект с именем. Используется как базовый предок.
 * Имя объекта по умолчанию регистронезависимое.
 */
public class Named implements INamed, INamedSet {

    protected String name;

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Имеется ли установленное имя
     */
    public boolean hasName() {
        return name != null && name.length() > 0;
    }

    /**
     * Проверка на совпадение имени без учета регистра.
     */
    public boolean hasName(String name) {
        return this.name != null && this.name.equalsIgnoreCase(name);
    }

}
