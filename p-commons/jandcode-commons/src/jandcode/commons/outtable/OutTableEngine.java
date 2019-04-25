package jandcode.commons.outtable;

import jandcode.commons.error.*;
import jandcode.commons.outtable.impl.*;

import java.util.*;

/**
 * Поддержка OutTable
 */
public class OutTableEngine {

    private List<OutTableFactory> factorys = new ArrayList<>();

    class DefaultOutTableFactory implements OutTableFactory {

        public OutTable createOutTable(Object data) {
            if (data instanceof Map) {
                return new MapOutTable((Map) data);
            }
            return null;
        }
    }

    public OutTableEngine() {
        this.factorys.add(new DefaultOutTableFactory());
    }

    /**
     * Создать {@link OutTableSaver} для указанного объекта.
     *
     * @param data объект с данными. Может быть как объектом типа {@link OutTable},
     *             так и объектом, который можно к нему привести
     */
    public OutTableSaver createOutTableSaver(Object data) {
        OutTable tb = createOutTable(data);
        return new OutTableSaverImpl(tb);
    }

    /**
     * Создать {@link OutTable} для указанного объекта.
     *
     * @param data если тип {@link OutTable}, то возвращается он сам.
     *             иначе производиься попытка привести объект к типу {@link OutTable}.
     */
    public OutTable createOutTable(Object data) {
        if (data instanceof OutTable) {
            return (OutTable) data;
        }
        if (data == null) {
            throw new XError("Нельзя создать OutTable для null");
        }
        for (OutTableFactory f : this.factorys) {
            OutTable res = f.createOutTable(data);
            if (res != null) {
                return res;
            }
        }
        throw new XError("Нельзя создать OutTable для {0}", data.getClass().getName());
    }

    /**
     * Зарегистрировать глобальную фабрику OutTable
     */
    public void registerOutTableFactory(OutTableFactory f) {
        this.factorys.add(0, f);
    }

}
