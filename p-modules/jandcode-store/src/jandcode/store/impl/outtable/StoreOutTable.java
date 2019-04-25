package jandcode.store.impl.outtable;

import jandcode.commons.*;
import jandcode.commons.outtable.*;
import jandcode.commons.variant.*;
import jandcode.store.*;

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
            return "" + r.getDictText(f.getName()) + "[" + r.getString(col) + "]";
        }
    }

    public VariantDataType getColDataType(int col) {
        return store.getField(col).getDataType();
    }

}
