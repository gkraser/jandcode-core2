package jandcode.commons.conf.impl;

import jandcode.commons.conf.*;

/**
 * Контейнер для Conf с весом и индексом для сортировки
 */
public class ConfWeightItem implements Comparable<ConfWeightItem> {

    private Conf conf;
    private int weight;
    private int index;

    public ConfWeightItem(Conf conf, int weight, int index) {
        this.conf = conf;
        this.weight = weight;
        this.index = index;
    }

    /**
     * Вес берем из атрибута weight
     */
    public ConfWeightItem(Conf conf, int index) {
        this.conf = conf;
        this.weight = conf.getInt("weight", 50);
        this.index = index;
    }

    /**
     * Вес берем из атрибута weight, index не важен
     */
    public ConfWeightItem(Conf conf) {
        this(conf, -1);
    }

    public Conf getConf() {
        return conf;
    }

    public int getWeight() {
        return weight;
    }

    public int getIndex() {
        return index;
    }

    public int compareTo(ConfWeightItem o) {
        Integer i1 = weight;
        Integer i2 = o.weight;
        int n = i1.compareTo(i2);
        if (n == 0) {
            Integer s1 = index;
            int s2 = o.index;
            n = s1.compareTo(s2);
        }
        return n;
    }

}
