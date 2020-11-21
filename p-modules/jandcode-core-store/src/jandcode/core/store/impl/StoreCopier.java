package jandcode.core.store.impl;

import jandcode.core.store.*;

import java.util.*;

/**
 * Копировщик данных для Store
 */
public class StoreCopier {

    protected List<FieldLink> fieldLink = new ArrayList<>();

    public static class FieldLink {
        public int srcIndex;
        public int dstIndex;

        public FieldLink(StoreField src, StoreField dst) {
            this.srcIndex = src.getIndex();
            this.dstIndex = dst.getIndex();
        }
    }

    public StoreCopier(Store src, Store dst) {
        createFieldLink(src, dst);
    }

    private void createFieldLink(Store src, Store dst) {
        for (StoreField f1 : src.getFields()) {
            StoreField f2 = dst.findField(f1.getName());
            if (f2 != null) {
                fieldLink.add(new FieldLink(f1, f2));
            }
        }
    }

    public void copyRecord(StoreRecord src, StoreRecord dst) {
        for (FieldLink link : fieldLink) {
            if (src.isValueNull(link.srcIndex)) {
                dst.setValue(link.dstIndex, null);
            } else {
                dst.setValue(link.dstIndex, src.getValue(link.srcIndex));
            }
        }
    }

    public void copyStore(Store src, Store dst) {
        for (StoreRecord rec : src) {
            StoreRecord recNew = dst.add();
            copyRecord(rec, recNew);
        }
    }

}
