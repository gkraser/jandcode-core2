package jandcode.commons.idgen;

/**
 * Генератор уникальных id. Реализация по умолчанию.
 */
public class IdGeneratorDefault implements IdGenerator {

    private String prefix;
    private String baseId;
    private int lastId;
    private boolean sync;

    /**
     * Создание генратора
     *
     * @param prefix префикс id
     * @param sync   true - получение id должно быть синхронизированным. Это нужно в случае,
     *               если экземпляр разделяется между потоками. Если он используется только
     *               в одном потоке, укажите false, работать будет быстрее.
     */
    public IdGeneratorDefault(String prefix, boolean sync) {
        if (prefix == null) {
            prefix = "";
        }
        this.prefix = prefix;
        this.sync = sync;
        this.baseId = Long.toString(System.currentTimeMillis(), 36) + "-";
    }

    public String nextId(String prefix) {
        if (prefix == null) {
            prefix = this.prefix;
        }
        if (sync) {
            synchronized (this) {
                lastId++;
                return prefix + baseId + lastId;
            }
        } else {
            lastId++;
            return prefix + baseId + lastId;
        }
    }

    public String nextId() {
        return nextId(prefix);
    }

}
