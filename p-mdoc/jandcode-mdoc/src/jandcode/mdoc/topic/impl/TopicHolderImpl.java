package jandcode.mdoc.topic.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.mdoc.topic.*;

import java.util.*;

public class TopicHolderImpl implements TopicHolder {

    private Map<String, Topic> items = new LinkedHashMap<>();
    private Map<String, Topic> itemsByFile = new LinkedHashMap<>();

    public void add(Topic topic) {
        items.put(topic.getId(), topic);
        itemsByFile.put(topic.getSourceFile().getPath(), topic);
    }

    public Collection<Topic> getItems() {
        return items.values();
    }

    public Topic find(String id) {
        Topic res = items.get(id);
        if (res == null) {
            id = UtVDir.normalize(id);
            res = itemsByFile.get(id);
        }
        return res;
    }

    public Topic get(String id) {
        Topic a = find(id);
        if (a == null) {
            throw new XError("Не найдена статья {0}", id);
        }
        return a;
    }

    public Iterator<Topic> iterator() {
        return getItems().iterator();
    }

}
