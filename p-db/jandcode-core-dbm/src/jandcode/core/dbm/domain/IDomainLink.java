package jandcode.core.dbm.domain;

/**
 * Интерфейс для объектов, которые знают про свой домен
 */
public interface IDomainLink {

    /**
     * Домен
     */
    Domain getDomain();

}
