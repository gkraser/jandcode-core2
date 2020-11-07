package jandcode.mdoc.cm.snippet;

import jandcode.commons.*;

import java.util.regex.*;

public class VueSnippet extends Snippet {

    public static final String TEMPLATE = "template";
    public static final String TEMPLATE_ALL = "template-all";
    public static final String SCRIPT = "script";
    public static final String SCRIPT_ALL = "script-all";
    public static final String STYLE = "style";

    private static Pattern p_template = Pattern.compile("<template.*?>(.*)</template>", Pattern.MULTILINE | Pattern.DOTALL);
    private static Pattern p_script = Pattern.compile("<script.*?>(.*)</script>", Pattern.MULTILINE | Pattern.DOTALL);
    private static Pattern p_style = Pattern.compile("<style.*?>(.*)</style>", Pattern.MULTILINE | Pattern.DOTALL);

    protected void makeParts(String text) {

        Matcher m;

        m = p_template.matcher(text);
        if (m.find()) {
            String s = m.group(1);
            s = UtString.normalizeIndent(s);
            addPart(TEMPLATE, s, "html");
            //
            XmlSnippet jsn = new XmlSnippet();
            jsn.configure(s, "html");
            //
            for (SnippetPart sp : jsn.getParts()) {
                if (CONTENT_ALL.equals(sp.getName())) {
                    addPart(TEMPLATE_ALL, sp.getText(), "html");
                } else if (CONTENT.equals(sp.getName())) {
                    addPart(TEMPLATE, sp.getText(), "html");
                } else {
                    addPart(sp.getName(), sp.getText(), sp.getLang());
                }
            }
        }

        m = p_script.matcher(text);
        if (m.find()) {
            String s = m.group(1);
            s = UtString.normalizeIndent(s);
            addPart(SCRIPT, s, "js");
            //
            JavaSnippet jsn = new JavaSnippet();
            jsn.configure(s, "js");
            //
            for (SnippetPart sp : jsn.getParts()) {
                if (CONTENT_ALL.equals(sp.getName())) {
                    addPart(SCRIPT_ALL, sp.getText(), "js");
                } else if (CONTENT.equals(sp.getName())) {
                    addPart(SCRIPT, sp.getText(), "js");
                } else {
                    addPart(sp.getName(), sp.getText(), sp.getLang());
                }
            }
        }

        m = p_style.matcher(text);
        if (m.find()) {
            String s = m.group(1);
            s = UtString.normalizeIndent(s);
            addPart(STYLE, s, "css");
        }

        /// собираем content, с убранными маркерами
        StringBuilder sbContent = new StringBuilder();

        SnippetPart sp;
        sp = getParts().find(TEMPLATE);
        if (sp != null) {
            sbContent.append("<template>\n");
            sbContent.append(UtString.indent(sp.getText(), 4));
            sbContent.append("\n</template>");
        }

        sp = getParts().find(SCRIPT);
        if (sp != null) {
            if (sbContent.length() > 0) {
                sbContent.append("\n\n");
            }
            sbContent.append("<script>\n");
            sbContent.append(sp.getText());
            sbContent.append("\n</script>");
        }

        sp = getParts().find(STYLE);
        if (sp != null) {
            if (sbContent.length() > 0) {
                sbContent.append("\n\n");
            }
            sbContent.append("<style>\n");
            sbContent.append(sp.getText());
            sbContent.append("\n</style>");
        }

        addPart(CONTENT, sbContent.toString(), "vue");
    }

}
