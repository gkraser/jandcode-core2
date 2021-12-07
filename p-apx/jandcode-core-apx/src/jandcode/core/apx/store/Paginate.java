package jandcode.core.apx.store;

import jandcode.commons.*;

import java.util.*;

/**
 * Информация о пагинации.
 * Используется в качестве параметра dao-метода.
 * Так же с помощью этого объекта передается информация о пагинации в store
 * для клиента.
 *
 * @see ApxStoreUtils#setPaginate(jandcode.core.store.Store, jandcode.core.apx.store.Paginate)
 */
public class Paginate {

    private int offset;
    private int limit;
    private int total;

    public Paginate() {
    }

    public Paginate(Paginate src) {
        this(src.offset, src.limit, src.total);
    }

    public Paginate(Map src) {
        if (src != null) {
            this.offset = UtCnv.toInt(src.get("offset"));
            this.limit = UtCnv.toInt(src.get("limit"));
            this.total = UtCnv.toInt(src.get("total"));
        }
    }

    public Paginate(int offset, int limit, int total) {
        this.offset = offset;
        this.limit = limit;
        this.total = total;
    }

    public Paginate(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
    }

    public String toString() {
        return "Paginate{" +
                "offset=" + offset +
                ", limit=" + limit +
                ", total=" + total +
                '}';
    }

    /**
     * Смещение от начала набора данных, наиная с 0
     */
    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * Сколько записей требуется
     */
    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Сколько всего записей имеется
     */
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
