package jandcode.core.store.impl.outtable;

import jandcode.commons.*;
import jandcode.commons.outtable.*;
import jandcode.commons.variant.*;
import jandcode.core.store.*;

import java.util.*;

/**
 * Представление Store как OutTable.
 */
public class StoreOutTable implements OutTable {

    private Store store;
    private List<StoreRecord> records;

    public StoreOutTable(Store store) {
        this.store = store;
        this.records = store.getRecords();
    }

    public StoreOutTable(StoreRecord record) {
        this.store = record.getStore();
        this.records = Collections.singletonList(record);
    }

    public StoreOutTable(Store store, List<StoreRecord> records) {
        this.store = store;
        this.records = records;
    }

    public int getCountCols() {
        return store.getCountFields();
    }

    public String getColTitle(int col) {
        return store.getField(col).getName();
    }

    public int getCountRows() {
        return records.size();
    }

    public Object getCell(int row, int col) {
        StoreField f = store.getField(col);
        StoreRecord r = records.get(row);
        if (r.isValueNull(col)) {
            return null;
        }
        if (UtString.empty(f.getDict())) {
            return r.getValue(col);
        } else {
            Object v = r.getValue(col);
            StringBuilder sb = new StringBuilder();
            sb.append(UtCnv.toString(v)).append(" [");
            IStoreDictResolver dr = r.getStore().getDictResolver();
            if (dr == null) {
                sb.append(OutTableSaver.NULL_STRING_VALUE);
                return sb.toString();
            }
            if (v instanceof CharSequence || v instanceof Collection) {
                List<String> lst = UtCnv.toList(r.getValue(col));
                int idx = 0;
                for (String vs : lst) {
                    Object dv = dr.getDictValue(f.getDict(), vs, null);
                    if (idx != 0) {
                        sb.append(", ");
                    }
                    if (dv == null) {
                        sb.append(OutTableSaver.NULL_STRING_VALUE);
                    } else {
                        sb.append(UtCnv.toString(dv));
                    }
                    idx++;
                }
            } else {
                sb.append(dr.getDictValue(f.getDict(), v, null));
            }
            sb.append("]");
            return sb.toString();
        }
    }

    public VariantDataType getColDataType(int col) {
        StoreField f = store.getField(col);
        if (!UtString.empty(f.getDict())) {
            return VariantDataType.STRING; // со словарями считаем строками
        } else {
            return f.getStoreDataType().getDataType();
        }
    }

}
