package jandcode.core.apx.store;

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

    public Paginate(int offset, int limit, int total) {
        this.offset = offset;
        this.limit = limit;
        this.total = total;
    }

    public Paginate(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
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
