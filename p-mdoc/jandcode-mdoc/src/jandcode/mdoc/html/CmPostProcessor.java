package jandcode.mdoc.html;

import jandcode.mdoc.builder.*;
import jandcode.mdoc.cm.*;

import java.util.regex.*;

/**
 * Обработчик тегов {@code <cm>}
 */
public class CmPostProcessor extends BaseOutBuilderMember implements HtmlPostProcessor {

    private static Pattern cmPattern = Pattern.compile("<cm>(.*?)</cm>", Pattern.MULTILINE | Pattern.DOTALL);

    public String process(String html, OutFile outFile) {
        CmService cm = getOutBuilder().bean(CmService.class);

        Matcher m = cmPattern.matcher(html);

        StringBuilder sb = new StringBuilder();
        int start = 0;
        while (m.find()) {
            sb.append(html, start, m.start());

            String cmText = m.group(1);
            String res = cm.resolveCm(cmText, outFile);

            sb.append(res);

            start = m.end();
        }
        if (start == 0) {
            return html;
        }

        sb.append(html.substring(start));

        return sb.toString();
    }


}
