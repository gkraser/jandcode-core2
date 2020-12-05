package jandcode.core.dao;

import jandcode.commons.conf.*;
import jandcode.core.*;

import java.util.*;

/**
 * Провайдер для предоставления списка элементов {@link DaoHolderItem}
 */
public interface DaoHolderItemProvider extends Comp {

    /**
     * Загрузить список элементов.
     *
     * @param conf       конфигурация 'item' в 'holder'
     * @param namePrefix префикс для имен
     * @return список, возможно пустой
     */
    Collection<DaoHolderItem> loadItems(Conf conf, String namePrefix);

}
