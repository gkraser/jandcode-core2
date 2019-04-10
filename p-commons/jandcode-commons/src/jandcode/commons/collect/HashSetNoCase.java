package jandcode.commons.collect;

import java.util.*;

public class HashSetNoCase extends HashSet<String> {

    public boolean add(String s) {
        s = s.toLowerCase();
        return super.add(s);
    }

    public boolean contains(Object key) {
        key = ((String) key).toLowerCase();
        return super.contains(key);
    }

    public boolean remove(Object key) {
        key = ((String) key).toLowerCase();
        return super.remove(key);
    }
}
