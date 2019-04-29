package jandcode.commons.collect;

import jandcode.commons.*;

import java.util.*;

/**
 * Раскрывальщик свойств из map с подстановками '${var}', где var - другое свойство.
 */
public class PropsExpander implements ISubstVar {

    private Map<String, Object> rawProps;
    private Set<String> stack = new HashSet<>();
    private String cycleValue = "";

    /**
     * Создание экземпляра для указанной Map
     *
     * @param rawProps произвольная map
     */
    public PropsExpander(Map<String, Object> rawProps) {
        this.rawProps = rawProps;
    }

    /**
     * Установить значение, которое будет подставлятся, если возникнет
     * циклическая цепочка в подстановках. По умолчанию - пустая строка.
     */
    public void setCycleValue(String cycleValue) {
        this.cycleValue = cycleValue;
    }

    /**
     * Раскрыть строку с подстановками
     *
     * @param value произволная строка с подстановками
     * @return раскрытая строка
     */
    public String expandValue(Object value) {
        return UtString.substVar(UtString.toString(value), this);
    }

    /**
     * Возвращает раскрытое значение указанного свойства.
     *
     * @param name имя свойста
     */
    public String expandProp(String name) {
        if (stack.contains(name)) {
            return cycleValue;
        }
        stack.add(name);
        try {
            return expandValue(rawProps.get(name));
        } finally {
            stack.remove(name);
        }
    }

    /**
     * Возвращает map, где все знаячени свойст раскрыты.
     */
    public Map<String, Object> expandAll() {
        Map<String, Object> res = new LinkedHashMap<>();
        for (String key : rawProps.keySet()) {
            res.put(key, expandProp(key));
        }
        return res;
    }

    //////

    public String onSubstVar(String v) {
        if (!rawProps.containsKey(v)) {
            return "";
        }
        return expandProp(v);
    }

}
