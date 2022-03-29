package jandcode.core.dbm.domain;

import jandcode.commons.named.*;

import java.util.*;

/**
 * Ссылки домена
 */
public class DomainRefs implements INamed {

    private Domain domain;
    private List<Ref> outRefs = new ArrayList<>();
    private List<Ref> inRefs = new ArrayList<>();

    public static class Ref {
        private Field field;
        private Domain from;
        private Domain to;

        public Ref(Field field, Domain from, Domain to) {
            this.field = field;
            this.from = from;
            this.to = to;
        }

        public Field getField() {
            return field;
        }

        public Domain getFrom() {
            return from;
        }

        public Domain getTo() {
            return to;
        }
    }

    public DomainRefs(Domain domain) {
        this.domain = domain;
    }

    /**
     * Имя домена
     */
    public String getName() {
        return getDomain().getName();
    }

    /**
     * Домен
     */
    public Domain getDomain() {
        return domain;
    }

    /**
     * Куда ссылается
     */
    public Collection<Ref> getOutRefs() {
        return outRefs;
    }

    /**
     * Кто ссылается
     */
    public Collection<Ref> getInRefs() {
        return inRefs;
    }

    /**
     * Все ссылки, уникальные по полям
     */
    public Collection<Ref> getRefs() {
        Map<Field, Ref> tmp = new HashMap<>();
        for (Ref r: getInRefs()) {
            tmp.put(r.getField(), r);
        }
        for (Ref r: getOutRefs()) {
            tmp.put(r.getField(), r);
        }
        return tmp.values();
    }

    /**
     * Есть ли воообще ссылки
     */
    public boolean hasRefs() {
        return getOutRefs().size() > 0 || getInRefs().size() > 0;
    }

    /**
     * Все домены, которые учавствуют в ссылках, кроме самого домена
     */
    public NamedList<Domain> getDomains() {
        NamedList<Domain> res = new DefaultNamedList<>();
        for (Ref r: getInRefs()) {
            res.add(r.getFrom());
            res.add(r.getTo());
        }
        for (Ref r: getOutRefs()) {
            res.add(r.getFrom());
            res.add(r.getTo());
        }
        res.remove(getDomain());
        return res;
    }
}
