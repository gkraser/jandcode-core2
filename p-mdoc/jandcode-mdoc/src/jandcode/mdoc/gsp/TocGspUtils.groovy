package jandcode.mdoc.gsp

import jandcode.mdoc.topic.*

/**
 * Утилиты для генерации содержания
 */
class TocGspUtils {

    GspTemplateContext context

    TocGspUtils(GspTemplateContext context) {
        this.context = context
    }

    /**
     * Сформировать содержание как ul/li
     *
     * @param root какое содержание выводить
     * @param topic статья, чей элемент содержания должен быть активным.
     *          Может быть null, тогда выводится полное содержание
     */
    String makeToc(Toc root, Topic curTopic = null) {
        if (root == null) {
            return ""
        }
        Toc cur = null
        if (curTopic != null) {
            cur = root.findByTopic(curTopic.id)
        }
        StringBuilder sb = new StringBuilder()
        Map idx = [:]
        if (cur != null) {
            // формируем цепочку открытых узлов начиная с текущего и до корня
            idx.put(cur, "cur")
            Toc own = cur.owner
            while (own != null && !own.is(root)) {
                idx.put(own, "")
                own = own.owner
            }
        }
        makeToc_internal(sb, root, idx)
        return sb.toString()
    }

    private void makeToc_internal(StringBuilder sb, Toc t, Map idx) {
        if (t.childs.size() == 0) {
            return
        }
        sb.append("""<ul>""")
        for (ch in t.childs) {
            boolean isCur = idx[ch] == "cur"
            boolean isOpen = idx[ch] != null
            boolean hasChilds = ch.childs.size() > 0
            String cls = ""
            if (isOpen && hasChilds) {
                cls += "open "
            } else if (hasChilds) {
                cls += "folder "
            }
            if (isCur) {
                cls += "cur "
            }

            sb.append("""<li class="${cls}"><a href="${context.ref(ch)}">${
                ch.title
            }</a>""")
            if (idx.size() == 0 || idx.containsKey(ch)) {
                makeToc_internal(sb, ch, idx)
            }
            sb.append("""</li>\n""")
        }
        sb.append("""</ul>""")
    }

    ////// breadcrumb

    /**
     * Возвращает список toc для breadcrumb для указанной статьи.
     * Сама статья будет последней в списке.
     */
    List<Toc> getBreadcrumb(Toc root, Topic topic) {
        List<Toc> res = new ArrayList<>();
        Toc toc = root.findByTopic(topic.id);
        while (toc != null) {
            res.add(0, toc);
            toc = toc.getOwner();
        }
        return res;
    }

    /**
     * Генерация breadcrumb
     */
    String makeBreadcrumb(Toc root, Topic topic) {
        def lst = getBreadcrumb(root, topic)
        if (lst.size() == 0) {
            return
        }
        StringBuilder sb = new StringBuilder()
        sb.append("""<ul class="breadcrumb">\n""")
        for (int i = 0; i < lst.size(); i++) {
            Toc t1 = lst[i]
            if (i < lst.size() - 1) {
                sb.append("""<li><a href="${context.ref(t1)}">${
                    t1.getTitle()
                }</a></li>\n""")
            } else {
                sb.append("""<li class="active">${t1.getTitle()}</li>\n""")
            }
        }
        sb.append("""</ul>\n""")
        return sb.toString()
    }

}
