package jandcode.core.store.std;

import jandcode.commons.simxml.*;
import jandcode.core.store.*;

import java.io.*;
import java.util.*;

/**
 * Загрузка из xml.
 * <p>
 * Формат xml:
 * <pre>{@code
 * <root>
 *    <ANY-TAG fieldname1="fieldvalue"  ... "/>
 * </root>
 * }</pre>
 * <p>
 * В качестве значения поля может быть указан {@link StoreConsts#NULL_STRING_VALUE_XML},
 * тогда в поле явно запишется null.
 * <p>
 * Если структура store явно не установлена, она автоматом строится по всем атрибутам
 * всех строк.
 */
public class StoreLoader_xml extends BaseStoreLoader {

    public void doLoad(Reader reader, Store store, boolean createStruct) throws Exception {
        SimXml x = new SimXmlNode();
        x.load().fromReader(reader);
        //
        if (createStruct) {
            // store без структуры, определяем структуру
            createStoreStruct(store, x);
        }
        // грузим
        for (SimXml x1 : x.getChilds()) {
            StoreRecord rec = store.add();
            if (x1.hasAttrs()) {
                for (Map.Entry<String, Object> en : x1.getAttrs().entrySet()) {
                    StoreField f = store.findField(en.getKey());
                    if (f != null) {
                        Object value = en.getValue();
                        if (value == null) {
                            rec.setValue(en.getKey(), null);
                        } else if (value instanceof String && StoreConsts.isNullStringValue(value.toString())) {
                            rec.setValue(en.getKey(), null);
                        } else {
                            rec.setValue(en.getKey(), value);
                        }
                    }
                }
            }
        }

    }

    private void createStoreStruct(Store store, SimXml x) {
        for (SimXml x1 : x.getChilds()) {
            if (x1.hasAttrs()) {
                for (Map.Entry<String, Object> en : x1.getAttrs().entrySet()) {
                    if (store.findField(en.getKey()) == null) {
                        store.addField(en.getKey(), "string");
                    }
                }
            }
        }
    }

}
