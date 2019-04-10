package jandcode.flexmark.mdtopic.impl;

import com.vladsch.flexmark.ast.*;
import com.vladsch.flexmark.ext.yaml.front.matter.*;
import com.vladsch.flexmark.util.ast.*;
import jandcode.commons.*;

import java.util.*;

/**
 * Построитель внутреннего содержания
 * и заголовка статьи по AST
 */
public class MdVisitor extends NodeVisitor {

    private static Collection<VisitHandler<?>> dummyHandlers = new ArrayList<>();

    private boolean foundFirstHeader;
    private int idCnt;
    private MdTocImpl[] stack = new MdTocImpl[10];
    private MdVisitorData data;

    public MdVisitor() {
        super(dummyHandlers);
        addHandlers(new VisitHandler<>(YamlFrontMatterNode.class, this::visitFrontMatter));
        addHandlers(new VisitHandler<>(Heading.class, this::visitHead));
        this.data = new MdVisitorData();
        stack[0] = (MdTocImpl) this.data.getToc();
    }

    /**
     * Собранные из статьи данные
     */
    public MdVisitorData getData() {
        return data;
    }

    //////

    private void visitHead(Heading node) {
        if (!foundFirstHeader) {
            foundFirstHeader = true;

            // первый заголовок
            this.data.setTitle(extractText(node));

            // убираем из документа
            node.unlink();

        } else {
            String id = node.getAnchorRefId();
            if (UtString.empty(id)) {
                idCnt++;
                id = "head" + idCnt;
                node.setAnchorRefId(id);
            }
            String title = extractText(node);
            if (UtString.empty(title)) {
                title = id;
            }
            //
            int level = node.getLevel();
            // нормализуем
            if (level >= stack.length - 1) {
                level = stack.length - 1;
            }
            if (level < 1) {
                level = 1;
            }
            MdTocImpl curToc = new MdTocImpl(id, title, level);
            int n = 1;
            while (true) {
                if (stack[n] == null || level <= stack[n].getNodeLevel()) {
                    // пустое место
                    break;
                }
                n++;
            }
            stack[n] = curToc;
            stack[n - 1].getChilds().add(curToc);
            node.setLevel(n + 1);

            // чистим кеш справа
            n++;
            while (n < stack.length && stack[n] != null) {
                stack[n] = null;
                n++;
            }

        }
    }

    private void visitFrontMatter(YamlFrontMatterNode node) {
        String key = node.getKey();
        if (UtString.empty(key)) {
            return;
        }
        List<String> values = node.getValues();
        if (values == null || values.size() == 0) {
            return;
        }
        String value = values.get(values.size() - 1);
        if (value == null) {
            return;
        }
        value = value.trim();

        // title
        if ("title".equals(key)) {
            foundFirstHeader = true;
            this.data.setTitle(value);
        }

        this.data.getProps().put(key, value);
    }

    private String extractText(Block b) {
        Node t = b.getFirstChild();
        if (t instanceof Text) {
            return t.getChars().toString();
        }
        return "";
    }

}
