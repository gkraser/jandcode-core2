package jandcode.commons.outtable;

import jandcode.commons.variant.*;

/**
 * Интерфейс для представления объекта в виде таблицы для вывода.
 * Строки и колонки нумеруются с 0.
 */
public interface OutTable {

    /**
     * Сколько колонок
     */
    int getCountCols();

    /**
     * Заголовок колонки
     */
    String getColTitle(int col);

    /**
     * Тип данных в колонке.
     * Для выравнивания. Числа выравниваются вправо.
     *
     * @param col колонка
     */
    default VariantDataType getColDataType(int col) {
        return VariantDataType.STRING;
    }

    /**
     * Сколько строк
     */
    int getCountRows();

    /**
     * Значение для ячейки
     *
     * @param row номер строки
     * @param col номер колонки
     * @return значение, может быть null
     */
    Object getCell(int row, int col);


}
