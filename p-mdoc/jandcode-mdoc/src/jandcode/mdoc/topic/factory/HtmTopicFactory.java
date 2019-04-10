package jandcode.mdoc.topic.factory;

import jandcode.commons.*;
import jandcode.commons.attrparser.*;
import jandcode.core.*;
import jandcode.mdoc.*;
import jandcode.mdoc.source.*;
import jandcode.mdoc.topic.*;
import jandcode.mdoc.topic.impl.*;

import java.util.*;
import java.util.regex.*;

public class HtmTopicFactory extends BaseComp implements TopicFactory {

    public Topic createTopic(SourceFile sourceFile, Doc doc) {
        String sourceText = sourceFile.getText();

        TopicImpl res = new TopicImpl(sourceFile);

        // свойства из тега <mdoc-props>
        boolean hasTitleInProps = false;
        HtmlProps prp = extractProps(sourceText);
        if (prp != null) {
            sourceText = prp.text;
            res.getProps().putAll(prp.props);
            if (prp.props.containsKey("title")) {
                res.setTitle(prp.props.get("title"));
                hasTitleInProps = true;
            }
            if (prp.props.containsKey("titleShort")) {
                res.setTitleShort(prp.props.get("titleShort"));
            }
        }

        List<HtmlPart> parts = splitHtml(sourceText);

        HtmlPart part0 = null;
        if (!hasTitleInProps) {
            part0 = parts.remove(0);
            res.setTitle(part0.titleText);
        }

        fillToc(res.getToc(), parts);
        StringBuilder sb = new StringBuilder();
        if (part0 != null) {
            sb.append(part0.text).append("\n");
        }
        makeText(sb, parts);
        res.setBody(sb.toString());

        return res;
    }

    //////

    private static Pattern headerPattern = Pattern.compile(
            "<h([0-9])(.*?)>(.*?)</h\\1(?:.*?)>",
            Pattern.MULTILINE | Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

    private static Pattern mdocPropsPattern = Pattern.compile(
            "<mdoc-props>(.*?)</mdoc-props>", Pattern.MULTILINE | Pattern.DOTALL);

    class HtmlPart {
        int level;
        String titleText = "";
        String attrsText = "";
        StringBuilder text = new StringBuilder();
        boolean noHead;
    }

    class HtmlProps {
        String text;
        Map<String, String> props = new LinkedHashMap<>();
    }

    private List<HtmlPart> splitHtml(String text) {
        Matcher m = headerPattern.matcher(text);

        int start = 0;

        List<HtmlPart> parts = new ArrayList<>();
        HtmlPart lastPart = new HtmlPart();
        lastPart.level = 1;
        lastPart.noHead = true;
        parts.add(lastPart);

        while (m.find()) {

            lastPart.text.append(text, start, m.start());

            HtmlPart newPart = new HtmlPart();

            newPart.level = UtCnv.toInt(m.group(1));
            newPart.attrsText = m.group(2).trim();
            newPart.titleText = m.group(3).trim();

            if (UtString.empty(newPart.titleText)) {
                newPart.titleText = "No title";
            }

            start = m.end();

            parts.add(newPart);
            lastPart = newPart;
        }

        lastPart.text.append(text.substring(start));

        if (parts.size() > 1) {
            String s = parts.get(0).text.toString().trim();
            if (UtString.empty(s)) {
                // часть до первого заголовка пустая, убираем ее
                parts.remove(0);
            }
        }

        return parts;
    }

    private void fillToc(Toc root, List<HtmlPart> parts) {

        int idCnt = 0;
        Toc[] stack = new Toc[10];
        stack[0] = root;

        for (HtmlPart part : parts) {
            if (part.noHead) {
                continue;
            }
            AttrParser prs = new AttrParser();
            prs.loadFrom(part.attrsText);
            Map<String, String> attrs = prs.getResult();

            String id = attrs.get("id");
            if (UtString.empty(id)) {
                idCnt++;
                id = "head" + idCnt;
                part.attrsText += " id=\"" + id + "\"";
            }
            String title = part.titleText;
            if (UtString.empty(title)) {
                title = id;
            }
            //
            int level = part.level;
            // нормализуем
            if (level < 2) {
                level = 2;
            }
            if (level > stack.length - 1) {
                level = stack.length - 1;
            }
            level--;
            if (stack[level] == null) {
                // текущее место пустое, передвигаем в правильное место
                while (stack[level - 1] == null) {
                    level--;
                }
            }
            // все заголовки начиная с h2, h1 - это заголовок статьи
            part.level = level + 1;

            // делаем элемент содержания
            Toc curToc = new TocImpl();
            curToc.setTopic(root.getTopic()); // явно проставляем
            stack[level - 1].addChild(curToc);
            curToc.setSection(id);
            curToc.setTitle(title);
            stack[level] = curToc;

            // чистим кеш справа
            int n = level + 1;
            while (n < stack.length && stack[n] != null) {
                stack[n] = null;
                n++;
            }

        }
    }

    private void makeText(StringBuilder sb, List<HtmlPart> parts) {
        for (HtmlPart part : parts) {
            if (!part.noHead) {
                sb.append("<h").append(part.level);
                if (!UtString.empty(part.attrsText)) {
                    sb.append(" ");
                    sb.append(part.attrsText);
                }
                sb.append(">");
                sb.append(part.titleText);
                sb.append("</h").append(part.level).append(">\n");
            }
            sb.append(part.text).append("\n");
        }
    }

    private HtmlProps extractProps(String text) {
        HtmlProps res = null;

        Matcher m = mdocPropsPattern.matcher(text);

        StringBuilder sb = new StringBuilder();
        int start = 0;
        while (m.find()) {
            sb.append(text, start, m.start());

            String propsText = m.group(1);
            if (res == null) {
                res = new HtmlProps();
                AttrParser prs = new AttrParser();
                prs.loadFrom(propsText);
                res.props.putAll(prs.getResult());
            }

            start = m.end();
        }
        if (start == 0) {
            return null;
        }

        sb.append(text.substring(start));
        res.text = sb.toString();
        return res;
    }

}
