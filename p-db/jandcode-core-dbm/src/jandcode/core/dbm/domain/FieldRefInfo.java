package jandcode.core.dbm.domain;

/**
 * Информация о ссылке поля в рамках определенной группы доменов
 */
public class FieldRefInfo {

    private Field field;
    private DomainGroup domainGroup;

    public FieldRefInfo(Field field, DomainGroup domainGroup) {
        this.field = field;
        this.domainGroup = domainGroup;
    }

    /**
     * Поле
     */
    public Field getField() {
        return field;
    }

    /**
     * В рамках какой группы осуществляется сбор информации
     */
    public DomainGroup getDomainGroup() {
        return domainGroup;
    }

    /**
     * Имя ссылки
     */
    public String getRef() {
        return this.field.getRef();
    }

    /**
     * Возвращает домен, на который ссылается поле.
     *
     * @return null, если ссылка не ведет домен из группы
     */
    public Domain getRefDomain() {
        Domain refDomain = this.domainGroup.getDomains().find(this.field.getRef());
        if (refDomain == null) {
            return null;
        }
        if (refDomain.findField("id") == null) {
            return null; // нет id в домене, куда ссылаемся
        }
        return refDomain;
    }

}
