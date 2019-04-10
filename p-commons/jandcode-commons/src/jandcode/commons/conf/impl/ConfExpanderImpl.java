package jandcode.commons.conf.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;

import java.util.*;

public class ConfExpanderImpl implements ConfExpander {

    private Conf root;
    private Map<String, TypeDef> types = new LinkedHashMap<>();
    private Conf cache = UtConf.create();

    // общие параметры для всех типов
    private TypeDef anyType;

    private String parentAttrName = "parent";

    //////

    class TypeDef {
        String name;
        Conf items;
        Map<String, PropDef> props = new LinkedHashMap<>();
        List<String> notInherited = new ArrayList<>();
        List<String> notInheritedPrefix = new ArrayList<>();
        List<ConfParentResolver> parentResolvers = new ArrayList<>();

        public TypeDef(String name, Conf items) {
            this.name = name;
            this.items = items;
        }

        void addProp(PropDef p) {
            this.props.put(p.name, p);
        }

        Conf getObject(String name) {
            Object ob = this.items.getValue(name);
            if (ob instanceof Conf) {
                return (Conf) ob;
            }
            throw new XError("Не найден объект [{0}] типа [{1}]", name, this.name);
        }

        List<Conf> getParents(String startName) {
            List<Conf> parents = new ArrayList<>();
            Conf cur = getObject(startName);
            parents.add(cur);
            while (true) {
                String parent = cur.getString(parentAttrName);
                if (UtString.empty(parent)) {
                    break;
                }
                Conf p = getObject(parent);
                if (parents.indexOf(p) != -1) {
                    throw new XError("Циклическая зависимость");
                }
                parents.add(p);
                cur = p;
            }
            return parents;
        }

        void addNotInherited(String prop) {
            if (prop.endsWith("*")) {
                this.notInheritedPrefix.add(prop.substring(0, prop.length() - 1));
            } else {
                this.notInherited.add(prop);
            }
        }

        void removeNotInherited(Conf conf) {
            // удалить не наследуемые
            if (this.notInherited.size() > 0) {
                for (String nip : this.notInherited) {
                    conf.remove(nip);
                }
            }
            if (this.notInheritedPrefix.size() > 0) {
                List<String> forRemove = new ArrayList<>();
                for (String pn : conf.keySet()) {
                    for (String nip : this.notInheritedPrefix) {
                        if (pn.startsWith(nip)) {
                            forRemove.add(pn);
                            break;
                        }
                    }
                }
                if (forRemove.size() > 0) {
                    for (String pn : forRemove) {
                        conf.remove(pn);
                    }
                }
            }
        }

        public Conf resolveObject(String parent) {
            if (parent.indexOf('/') == -1) {
                return expand(this.name, parent);
            }

            for (ConfParentResolver rv : this.parentResolvers) {
                String path = rv.resolveParent(this.name, parent);
                if (path != null) {
                    String[] ar = path.split("/");
                    if (ar.length < 2) {
                        throw new XError("Распознанный parent [{0}] имеет неправильную структуру (получен из [{1}])", path, parent);
                    }
                    expand(ar[0], ar[1]);
                    Conf res = cache.findConf(path);
                    if (res == null) {
                        throw new XError("Не найден предок [{0}] (получен из [{1}])", path, parent);
                    }
                    return res;
                }
            }

            throw new XError("Не распознан parent [{0}]", parent);
        }

        public void addParentResolver(ConfParentResolver parentResolver) {
            parentResolvers.add(0, parentResolver);
        }
    }

    //////

    class PropDef {
        String name;
        String type;
        boolean container;

        public PropDef(String name, String type, boolean container) {
            this.name = name;
            this.type = type;
            this.container = container;
        }

    }

    //////

    public ConfExpanderImpl(Conf root) {
        this.root = root;
        this.anyType = new TypeDef("ANY", UtConf.create());
        init();
    }

    private void init() {
        // все conf верхнего уровня рассматриваем как контейнеры типов
        for (Map.Entry<String, Object> en : root.entrySet()) {
            Object v = en.getValue();
            if (!(v instanceof Conf)) {
                continue;
            }
            TypeDef td = new TypeDef(en.getKey(), (Conf) v);
            types.put(td.name, td);
        }
    }

    //////

    public Conf getRoot() {
        return root;
    }

    //////

    private TypeDef getTypeDef(String type, boolean createIfNotExist) {
        TypeDef td = types.get(type);
        if (td == null) {
            if (createIfNotExist) {
                td = new TypeDef(type, UtConf.create());
                types.put(td.name, td);
            } else {
                throw new XError("Не найден тип [{0}]", type);
            }
        }
        return td;
    }

    public void addRuleContainer(String type, String prop, String elementType) {
        PropDef p = new PropDef(prop, elementType, true);
        getTypeDef(type, true).addProp(p);
    }

    public void addRuleObject(String type, String prop, String objectType) {
        PropDef p = new PropDef(prop, objectType, false);
        getTypeDef(type, true).addProp(p);
    }

    public void addRuleNotInherited(String type, String prop) {
        TypeDef td = getTypeDef(type, true);
        td.addNotInherited(prop);
    }

    public void addRuleNotInherited(String prop) {
        this.anyType.addNotInherited(prop);
    }

    public void addRuleParent(String type, ConfParentResolver parentResolver) {
        TypeDef td = getTypeDef(type, true);
        td.addParentResolver(parentResolver);
    }

    //////

    public Conf expand(String type) {
        TypeDef td = getTypeDef(type, false);
        for (String name : td.items.keySet()) {
            expand(type, name);
        }
        return this.cache.findConf(type);
    }

    public Conf expand(String type, String name) {
        String path = type + "/" + name;
        Conf res = this.cache.findConf(path);
        if (res != null) {
            return res;
        }

        // в кеше нет
        TypeDef td = getTypeDef(type, false);
        Conf ob = td.getObject(name);

        // первый проход, объекты верхнего уровня, без кеша
        Conf exp = expand1(ob, td);

        // помещаем в кеш
        Conf typeCache = this.cache.findConf(type, true);
        typeCache.setValue(name, exp);

        // раскрываем свойства
        expandProps(exp, td);

        return exp;
    }

    /**
     * Раскрыть объект ob типа type.
     * Только сам объект. Кеш не используется.
     */
    private Conf expand1(Conf ob, TypeDef type) {
        String parent = ob.getString(this.parentAttrName);
        Conf res;
        if (UtString.empty(parent)) {
            // предок не указан
            res = ob.cloneConf();

        } else {
            // предок указан
            List<Conf> parents = type.getParents(parent);

            // parents.size>0 точно, для последнего делаем clone
            res = parents.get(parents.size() - 1).cloneConf();

            // остальные накладываем
            for (int i = parents.size() - 2; i >= 0; i--) {
                res.join(parents.get(i));
            }

            // удаляем не наследуемые
            anyType.removeNotInherited(res);
            type.removeNotInherited(res);

            // накладываем заказанный, в том числе ненаследуемые попадают тут
            res.join(ob);

        }


        res.remove(this.parentAttrName);

        return res;
    }

    /**
     * Раскрыть свойство propName объекта ob типа type
     */
    private void expandProp(Conf ob, String propName, TypeDef type) {
        Object propValue = ob.getValue(propName);
        if (!(propValue instanceof Conf)) {
            return;
        }
        Conf pviConf = (Conf) propValue;
        String parent = pviConf.getString(this.parentAttrName);
        if (UtString.empty(parent)) {
            return;
        }

        // parent имеется
        Conf parentObject = type.resolveObject(parent);
        Conf newValue = parentObject.cloneConf();
        newValue.join(pviConf);
        ob.setValue(propName, newValue);

        // удаляем не наследуемые
        anyType.removeNotInherited(newValue);
        type.removeNotInherited(newValue);

        // удаляем утрибут parent
        newValue.remove(this.parentAttrName);

        expandProps(newValue, type);
    }

    /**
     * Раскрыть дочерние свойства.
     * Объект уже находится в кеше.
     *
     * @param ob   объект, который раскрывается
     * @param type тип этого объекта
     */
    private void expandProps(Conf ob, TypeDef type) {
        // раскрываем дочерние свойства
        if (type.props.size() > 0) {
            // есть определения свойств
            for (PropDef pd : type.props.values()) {
                TypeDef td = getTypeDef(pd.type, false);
                if (pd.container) {
                    Object pv = ob.getValue(pd.name);
                    if (pv instanceof Conf) {
                        // свойство есть и оно Conf
                        Conf pvConf = (Conf) pv;
                        // делаем копию имен свойств
                        List<String> tmpPropNames = new ArrayList<>(pvConf.keySet());
                        for (String itemName : tmpPropNames) {
                            expandProp(pvConf, itemName, td);
                        }
                    }

                } else {
                    expandProp(ob, pd.name, td);
                }
            }
        }
    }

}
