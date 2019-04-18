package jandcode.commons.collect;

import java.util.*;

/**
 * Блочный итератор по коллекции. За итерацию возвращает список из blockSize элементов
 * переданной в конструкторе коллекции
 */
public class CollectionBlockIterator implements Iterator<List>, Iterable<List> {

    private Collection _coll;
    private int _blockSize = 10;
    private List<List> _tmp = new ArrayList<List>();
    private int _pos;

    public CollectionBlockIterator(Collection coll) {
        _coll = coll;
        build();
    }

    public CollectionBlockIterator(Collection coll, int blockSize) {
        _coll = coll;
        _blockSize = blockSize;
        build();
    }

    private void build() {
        List<Object> tmp = new ArrayList<>();
        for (Object value : _coll) {
            tmp.add(value);
            if (tmp.size() >= _blockSize) {
                _tmp.add(tmp);
                tmp = new ArrayList<>();
            }
        }
        if (tmp.size() > 0) {
            _tmp.add(tmp);
        }
    }

    public boolean hasNext() {
        return _pos < _tmp.size();
    }

    public List next() {
        List r = _tmp.get(_pos);
        _pos++;
        return r;
    }

    public void remove() {
    }

    public Iterator<List> iterator() {
        _pos = 0;
        return this;
    }

}
