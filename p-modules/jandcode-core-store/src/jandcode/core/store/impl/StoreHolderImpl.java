package jandcode.core.store.impl;

import jandcode.commons.named.*;
import jandcode.core.store.*;

import java.util.*;

public class StoreHolderImpl implements StoreHolder {

    private NamedList<Store> items = new DefaultNamedList<>("store {0} не найден");

    public NamedList<Store> getItems() {
        return items;
    }

    public void add(Store store) {
        items.add(store);
    }

    public void add(String name, Store store) {
        store.setName(name);
        add(store);
    }

    public Store find(String name) {
        return items.find(name);
    }

    public Iterator<Store> iterator() {
        return items.iterator();
    }

}
