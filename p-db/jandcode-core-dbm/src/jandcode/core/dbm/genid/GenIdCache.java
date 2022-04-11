package jandcode.core.dbm.genid;

/**
 * Кеш genid. Для {@link GenIdDriver#getGenIdCache(GenId, long)}
 */
public interface GenIdCache {

    /**
     * Есть ли следующая id.
     * Если нет, то кеш закончился.
     */
    boolean hasNextId();

    /**
     * Получить следующий элемент из кеша.
     * Если кеш закончилcя, будет сгенерирована ошибка.
     */
    long getNextId();

}
