package jandcode.commons.outtable;

/**
 * Фабрика для {@link OutTable}
 */
public interface OutTableFactory {

    /**
     * Создать {@link OutTable} для указанного объекта.
     *
     * @param data произвольный объект
     * @return null, если не умеет создавать
     */
    OutTable createOutTable(Object data);

}
