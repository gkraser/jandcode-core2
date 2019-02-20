package jandcode.jc;

import jandcode.commons.*;
import jandcode.commons.named.*;

import java.util.*;

/**
 * Список библиотек
 */
public class ListLib extends DefaultNamedList<Lib> {

    /**
     * Возвращает список classpath для всех библиотек в списке
     */
    public List<String> getClasspath() {
        List<String> res = new ArrayList<String>();
        for (Lib z : this) {
            String cp = z.getClasspath();
            if (UtString.empty(cp)) {
                continue;
            }
            res.add(cp);
        }
        return res;
    }

    /**
     * Отсортировать список по зависимостям.
     * Если библиотека находится в списке зависимостей другой библиотеки,
     * она будет выше в списке. Остальные сортируются по имени.
     */
    public void sortByDepends() {

        class SortItem implements Comparable<SortItem> {
            private Lib lib;
            private String name;
            private Set<String> depends;

            SortItem(Lib lib) {
                this.lib = lib;
                this.name = lib.getName();
                this.depends = new HashSet<>();
                for (Lib z : lib.getDepends().getAll().getLibsAll()) {
                    this.depends.add(z.getName());
                }
            }

            public int compareTo(SortItem it) {
                if (this.depends.contains(it.name)) {
                    return 1;
                }
                if (it.depends.contains(this.name)) {
                    return -1;
                }
                return this.name.compareTo(it.name);
            }
        }

        List<SortItem> lst = new ArrayList<>();
        for (Lib z : this) {
            lst.add(new SortItem(z));
        }
        Collections.sort(lst);

        clear();
        for (SortItem it : lst) {
            add(it.lib);
        }

    }

}
