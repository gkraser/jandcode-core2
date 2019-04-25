package jandcode.commons.outtable;

import java.util.*;

/**
 * Представление Map как OutTable.
 */
public class MapOutTable implements OutTable {

    private List<Row> rows = new ArrayList<>();

    class Row {
        Object key;
        Object value;

        public Row(Object key, Object value) {
            this.key = key;
            this.value = value;
        }
    }

    public MapOutTable(Map m) {
        for (Object key : m.keySet()) {
            rows.add(new Row(key, m.get(key)));
        }
    }

    public int getCountCols() {
        return 2;
    }

    public String getColTitle(int col) {
        if (col == 0) {
            return "key";
        } else {
            return "value";
        }
    }

    public int getCountRows() {
        return rows.size();
    }

    public Object getCell(int row, int col) {
        Row r = rows.get(row);
        if (col == 0) {
            return r.key;
        } else {
            return r.value;
        }
    }

}
