package jandcode.commons.outtable.impl;


import jandcode.commons.*;
import jandcode.commons.io.*;
import jandcode.commons.outtable.*;
import jandcode.commons.variant.*;

import java.io.*;

/**
 * Запись Store в виде таблицы в текст для просмотра во время отладки.
 */
public class OutTableSaverImpl implements OutTableSaver {

    private OutTable outTable;
    private int limit = -1;

    /**
     * Создать для store
     */
    public OutTableSaverImpl(OutTable outTable) {
        this.outTable = outTable;
    }

    /**
     * Сколько записей выводить. По умолчанию - все
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    public SaveTo save() {
        return new SaveTo(this);
    }

    protected String getCellText(int row, int col) {
        Object v = outTable.getCell(row, col);
        if (v == null) {
            return NULL_STRING_VALUE;
        }
        return UtCnv.toString(v);
    }

    public void saveTo(Writer writer) throws Exception {

        OutTable tb = this.outTable;
        int countRows = tb.getCountRows();
        int countCols = tb.getCountCols();
        int[] maxWidths = new int[countCols];
        boolean[] alignRight = new boolean[countCols];

        // размер заголовков и выравнивание
        for (int col = 0; col < countCols; col++) {
            maxWidths[col] = tb.getColTitle(col).length();
            alignRight[col] = VariantDataType.isNumber(tb.getColDataType(col));
        }

        // размеры данных
        for (int row = 0; row < countRows; row++) {
            if (limit > 0 && row >= limit) {
                break;
            }
            for (int col = 0; col < countCols; col++) {
                String v = getCellText(row, col);
                maxWidths[col] = Math.max(maxWidths[col], v.length());
            }
        }

        // общая ширина
        int widthAll = 2;
        for (int col = 0; col < countCols; col++) {
            widthAll = widthAll + maxWidths[col] + 1;
        }

        // разделитель линий
        StringBuilder linedelim = new StringBuilder();
        linedelim.append("+");
        for (int col = 0; col < countCols; col++) {
            linedelim.append(UtString.repeat("-", maxWidths[col])).append("+");
        }
        linedelim.append("\n");

        writer.append(linedelim);

        // заголовки
        writer.append("|");
        for (int col = 0; col < countCols; col++) {
            writer.append(UtString.padCenter(tb.getColTitle(col), maxWidths[col])).append("|");
        }
        writer.append("\n");


        //
        writer.append(linedelim);

        int numrec = 0;
        boolean limited = false;
        // данные
        for (int row = 0; row < countRows; row++) {
            if (limit > 0 && numrec >= limit) {
                limited = true;
                break;
            }
            numrec++;
            writer.append("|");
            for (int col = 0; col < countCols; col++) {
                String val = getCellText(row, col);

                if (alignRight[col]) {
                    val = UtString.padLeft(val, maxWidths[col]);
                } else {
                    val = UtString.padRight(val, maxWidths[col]);
                }

                writer.append(val).append("|");
            }
            writer.append("\n");
        }

        //
        writer.append(linedelim);
        writer.append("records: ").append(UtCnv.toString(countRows));
        if (limited) {
            writer.append(", print records: ").append(UtCnv.toString(numrec));
        }

    }

}
