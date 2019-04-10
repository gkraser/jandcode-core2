package jandcode.jc;

import jandcode.commons.*;
import jandcode.commons.error.*;
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
     * она будет выше в списке.
     * Используется "Алгоритм Тарьяна" отсюда: https://ru.wikipedia.org/wiki/Топологическая_сортировка
     */
    public void sortByDepends() {

        class SortItem {
            private Lib lib;
            private String name;
            private Set<String> depends;
            private int color;

            SortItem(Lib lib) {
                this.lib = lib;
                this.name = lib.getName();
                this.depends = new HashSet<>();
                for (Lib z : lib.getDepends().getAll().getLibsAll()) {
                    this.depends.add(z.getName());
                }
            }

        }

        class Sorter {

            private static final int GRAY = 1;
            private static final int BLACK = 2;

            private Map<String, SortItem> map = new LinkedHashMap<>();
            private List<SortItem> res = new ArrayList<>();

            Sorter(ListLib src) {
                for (Lib z : src) {
                    SortItem it = new SortItem(z);
                    map.put(it.name, it);
                }
            }

            void sort() {
                for (SortItem it : map.values()) {
                    step(it);
                }
            }

            void step(SortItem it) {
                if (it.color == BLACK) {
                    return;
                }
                if (it.color == GRAY) {
                    throw new XError("Циклическая зависимость в {0}", it.name);
                }
                it.color = GRAY;
                for (String dep : it.depends) {
                    SortItem ch = map.get(dep);
                    if (ch != null) {
                        step(ch);
                    }
                }
                it.color = BLACK;
                res.add(it);
            }
        }

        Sorter srt = new Sorter(this);
        srt.sort();

        clear();
        for (SortItem it : srt.res) {
            add(it.lib);
        }

    }

}
