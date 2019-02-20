package jandcode.commons.test;

import jandcode.commons.error.*;

/**
 * Утилиты для памяти для тестов
 */
public class MemoryTestSvc extends BaseTestSvc {

    private MemoryInfo saved;

    /**
     * Текущее состояние памяти
     */
    public MemoryInfo cur() {
        return MemoryInfo.create();
    }

    /**
     * Сохранить текущее состояние памяти
     */
    public void save() {
        this.saved = cur();
    }

    /**
     * Разнича между записанным через {@link MemoryTestSvc#save()} и текущим,
     * на момент вызова, состоянием.
     */
    public MemoryInfo diff() {
        if (this.saved == null) {
            throw new XError("diff without save");
        }
        return MemoryInfo.diff(saved, cur());
    }

    /**
     * Перевести в строку
     */
    public String toString(MemoryInfo mi) {
        return String.format("memory: %,d  init: %,d  max: %,d  used: %,d",
                mi.getCommitted(), mi.getInit(), mi.getMax(), mi.getUsed());
    }

    /**
     * Напечатать на консоле
     */
    public void print(String prefix, MemoryInfo mi) {
        System.out.println("" + prefix + toString(mi));
    }

    /**
     * Напечатать на консоле
     */
    public void print(MemoryInfo mi) {
        print("", mi);
    }

    /**
     * Напечатать на консоле текущее состояние
     */
    public void printCur() {
        print("curr: ", cur());
    }

    /**
     * Напечатать на консоле текущую разницу
     */
    public void printDiff() {
        print("diff: ", diff());
    }

}
