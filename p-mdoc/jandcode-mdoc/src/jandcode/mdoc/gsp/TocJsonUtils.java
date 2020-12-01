package jandcode.mdoc.gsp;

import jandcode.commons.*;
import jandcode.mdoc.builder.*;
import jandcode.mdoc.topic.*;

import java.util.*;

/**
 * Утилиты для toc в виде json
 */
public class TocJsonUtils {

    private OutBuilder builder;

    public TocJsonUtils(OutBuilder builder) {
        this.builder = builder;
    }

    ////// toc json

    /**
     * Сформировать содержание как js (в виде строки)
     *
     * @param root какое содержание выводить
     */
    public String makeTocJsStr(String varName, Toc root) {
        return varName + "=" + UtJson.toJson(makeTocJson(root));
    }

    /**
     * Сформировать содержание как json
     *
     * @param root какое содержание выводить
     */
    public List<Map<String, Object>> makeTocJson(Toc root) {
        if (root == null) {
            return new ArrayList<>();
        }
        List<Map<String, Object>> items = new ArrayList<>();
        makeTocJson_internal(items, root);
        return items;
    }

    private void makeTocJson_internal(List<Map<String, Object>> items, Toc t) {
        for (Toc ch : t.getChilds()) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("title", ch.getTitle());
            Topic topic = ch.getTopic();
            if (topic != null) {
                m.put("topicId", topic.getId());
            }
            m.put("ref", ref(ch));
            items.add(m);
            if (ch.getChilds().size() > 0) {
                List<Map<String, Object>> childItems = new ArrayList<>();
                m.put("items", childItems);
                makeTocJson_internal(childItems, ch);
            }
        }
    }

    /**
     * Формирование ссылки на элемент содержания
     */
    public String ref(Toc toc) {
        if (toc == null) {
            return "";
        }

        String s = "";

        Topic topic = toc.getTopic();
        if (topic != null) {
            OutFile outFile = builder.getOutFiles().findByTopicId(topic.getId());
            if (outFile != null) {
                s = outFile.getPath();
            }
        }

        if (!UtString.empty(toc.getSection())) {
            s = s + "#" + toc.getSection();
        }

        return s;
    }

}
