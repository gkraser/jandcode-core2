package jandcode.core.dbm.domain;

/**
 * Интерфейс для объектов в контексте домена
 */
public interface IDomainMember extends IDomainLink {

    /**
     * Домен
     */
    void setDomain(Domain domain);

}
