package jandcode.commons.conf.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;

import java.util.*;

public class ConfImpl implements Conf {

    private static ConfOrigin DUMMY_ORIGIN = new ConfOrigin() {
        public String toString() {
            return "unknown";
        }
    };

    protected Map<String, Object> props;
    protected String name;
    protected ConfOriginImpl origin;

    public ConfImpl(String name) {
        this.name = name;
        this.props = new LinkedHashMap<>();
    }

    public ConfImpl(String name, ConfImpl wrap) {
        this.name = name;
        this.props = wrap.props;
        this.origin = wrap.origin;
    }

    ////// named

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasName() {
        return name != null && name.length() > 0 && !name.startsWith(ConfConsts.NONAME_KEY);
    }

    public boolean hasName(String name) {
        return this.name != null && this.name.equals(name);
    }

    //////

    public Object getValue(String path) {
        int a = path.lastIndexOf('/');
        if (a == -1) {
            return props.get(path);
        } else {
            Conf x = findConf(path.substring(0, a));
            if (x == null) {
                return null;
            }
            return x.getValue(path.substring(a + 1));
        }
    }

    protected Object toValue(Object value) {
        if (value == null) {
            return null;

        } else if (value instanceof Conf) {
            return value;

        } else if (value instanceof Map) {
            ConfImpl x = new ConfImpl(null);
            x.joinMap((Map) value);
            return x;

        } else if (value instanceof Collection) {
            ConfImpl x = new ConfImpl(null);
            x.joinCollection((Collection) value);
            return x;

        } else {
            return value;
        }
    }

    public void setValue(String path, Object value) {
        setValueInternal(path, value);
    }

    protected Object setValueInternal(String path, Object value) {
        if (path.length() == 1 && path.charAt(0) == ConfConsts.NONAME_KEY_CHAR) {
            if (value == null) {
                return null; // неизвестно что удалять
            }
            path = ConfConsts.NONAME_KEY + props.size();
            while (true) {
                // добиваемся уникальности
                if (props.containsKey(path)) {
                    path = path + "#" + props.size();
                } else {
                    break;
                }
            }
        }
        int a = path.lastIndexOf('/');
        if (a == -1) {
            if (value == null) {
                props.remove(path);
                return null;
            } else {
                String k = UtStrDedup.normal(path);
                Object v = toValue(value);
                if (v instanceof ConfImpl) {
                    v = new ConfImpl(k, (ConfImpl) v);
                }
                props.put(k, v);
                return v;
            }
        } else {
            if (value == null) {
                Conf x = findConf(path.substring(0, a));
                if (x == null) {
                    return null;
                }
                x.setValue(path.substring(a + 1), null);
                return null;
            } else {
                Conf x = findConf(path.substring(0, a), true);
                return ((ConfImpl) x).setValueInternal(path.substring(a + 1), value);
            }
        }
    }

    public Conf cloneConf() {
        ConfImpl x = new ConfImpl(this.name);
        x.origin = this.origin;
        for (Entry<String, Object> en : props.entrySet()) {
            Object v = en.getValue();
            if (v instanceof Conf) {
                x.props.put(en.getKey(), ((Conf) v).cloneConf());
            } else {
                x.props.put(en.getKey(), v);
            }
        }
        return x;
    }

    public String toString() {
        return props.toString();
    }

    public ConfOrigin origin() {
        if (this.origin == null) {
            return DUMMY_ORIGIN;
        }
        return this.origin;
    }

    public Conf getConf(String path) {
        Object v = getValue(path);
        if (v == null) {
            throw new XError("Нет свойства {0}", path);
        } else if (v instanceof Conf) {
            return (Conf) v;
        } else {
            throw new XError("Тип значения свойства не Conf: {0}", path);
        }
    }

    public Conf findConf(String path) {
        return findConf(path, false);
    }

    public Conf findConf(String path, boolean createIfNotExist) {
        if (UtString.empty(path)) {
            return this;
        }
        String[] ar = path.split("/+");
        Conf cur = this;
        for (int i = 0; i < ar.length; i++) {
            Object v = cur.getValue(ar[i]);
            boolean yes = v instanceof Conf;
            if (!yes) {
                if (createIfNotExist) {
                    // нужно получить тот, который реально записался!
                    v = ((ConfImpl) cur).setValueInternal(ar[i], new ConfImpl(null));
                } else {
                    return null;
                }
            }
            cur = (Conf) v;
        }

        return cur;
    }

    public Collection<Conf> getConfs() {
        List<Conf> res = new ArrayList<>();

        for (Object v : props.values()) {
            if (v instanceof Conf) {
                res.add((Conf) v);
            }
        }

        return res;
    }

    public Collection<Conf> getConfs(String path) {
        Conf p = findConf(path);
        if (p == null) {
            return new ArrayList<>();
        } else {
            return p.getConfs();
        }
    }

    //////

    public void join(Object data) {
        if (data instanceof Conf) {
            joinConf((Conf) data);

        } else if (data instanceof Map) {
            joinMap((Map) data);

        } else if (data instanceof Collection) {
            joinCollection((Collection) data);

        }
        // ignore another
    }

    private void joinMap(Map data) {
        for (Object key : data.keySet()) {
            setValue(UtString.toString(key), data.get(key));
        }
    }

    private void joinCollection(Collection data) {
        for (Object value : data) {
            setValue(ConfConsts.NONAME_KEY, value);
        }
    }

    private void joinConf(Conf data) {
        ConfImpl dataConf = (ConfImpl) data;
        if (dataConf.origin != null) {
            this.origin = dataConf.origin;
        }
        for (Map.Entry<String, Object> en : dataConf.props.entrySet()) {
            String propName = en.getKey();
            Object newValue = en.getValue();

            if (propName.length() > 0 && propName.charAt(0) == ConfConsts.NONAME_KEY_CHAR) {
                // noname
                if (newValue instanceof Conf) {
                    // новое значение Conf: создаем новый Conf
                    setValue(ConfConsts.NONAME_KEY, ((Conf) newValue).cloneConf());
                } else {
                    // новое значение не conf: просто записываем
                    setValue(ConfConsts.NONAME_KEY, newValue);
                }

            } else {
                // определенное имя
                Object existValue = getValue(propName);
                if (newValue instanceof Conf) {
                    // новое значение Conf
                    if (existValue instanceof Conf) {
                        // существующее Conf - объединяем
                        ((ConfImpl) existValue).joinConf((Conf) newValue);
                    } else {
                        // существующее не Conf, заменяем на новый Conf
                        setValue(propName, ((Conf) newValue).cloneConf());
                    }
                } else {
                    // новое значение - не Conf
                    if (existValue instanceof Conf) {
                        // существующее Conf, игнорируем
                    } else {
                        // существующее не Conf (или нету) - записываем новое
                        setValue(propName, newValue);
                    }
                }
            }
        }
    }

    ////// map

    public Object get(String key, Object defValue) {
        Object v = getValue(key);
        if (v == null) {
            return defValue;
        } else {
            return v;
        }
    }

    public int size() {
        return props.size();
    }

    public boolean isEmpty() {
        return props.isEmpty();
    }

    public boolean containsKey(Object key) {
        return props.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return props.containsValue(value);
    }

    public Object get(Object key) {
        return getValue(UtCnv.toString(key));
    }

    public Object put(String key, Object value) {
        setValue(key, value);
        return null;
    }

    public Object remove(Object key) {
        return props.remove(key);
    }

    public void putAll(Map<? extends String, ?> m) {
        int size = m.size();
        if (size > 0) {
            for (Entry<? extends String, ?> entry : m.entrySet()) {
                setValue(entry.getKey(), entry.getValue());
            }
        }
    }

    public Set<String> keySet() {
        return props.keySet();
    }

    public Collection<Object> values() {
        return props.values();
    }

    public Set<Entry<String, Object>> entrySet() {
        return props.entrySet();
    }

    public void clear() {
        props.clear();
    }

}
