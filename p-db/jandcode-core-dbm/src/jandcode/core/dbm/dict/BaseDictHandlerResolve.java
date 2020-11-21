package jandcode.core.dbm.dict;

import jandcode.commons.collect.*;
import jandcode.core.dbm.mdb.*;
import jandcode.core.store.*;

import java.util.*;

/**
 * Базовый предок для обработчиков resolve-словарей.
 */
public abstract class BaseDictHandlerResolve implements DictHandler {

    /**
     * Загрузить блок ids.
     */
    protected abstract void resolveIdsBlock(Mdb mdb, Dict dict, Store data, Collection<Object> ids) throws Exception;

    //////

    private int blockSize = 200;

    /**
     * Размер блока. По умолчанию 200.
     */
    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    /**
     * Реализация по умолчанию: разбиваем набор ids на блоки размером
     * {@link BaseDictHandlerResolve#getBlockSize()} и для каждого блока вызываем
     * resolveIdsBlock()
     */
    public void resolveIds(Mdb mdb, Dict dict, Store data, Collection<Object> ids) throws Exception {
        if (ids.size() == 0) {
            return;
        }
        CollectionBlockIterator z = new CollectionBlockIterator(ids, getBlockSize());
        for (List itms : z) {
            resolveIdsBlock(mdb, dict, data, itms);
        }
    }

}
