package jandcode.mdoc.html;

import jandcode.commons.*;
import jandcode.mdoc.*;
import jandcode.mdoc.builder.*;

import java.util.regex.*;

/**
 * Обработка ссылок.
 * Распознанные ссылки заменяются на маркер ссылки.
 */
public class RefPostProcessor extends BaseOutBuilderMember implements HtmlPostProcessor {

    private static Pattern refPattern = Pattern.compile("(<a.*?href.*?=\")(.*?)(\".*?>)(.*?)(</a>)", Pattern.MULTILINE | Pattern.DOTALL);

    public String process(String html, OutFile outFile) {
        Matcher m = refPattern.matcher(html);

        RefResolver refResolver = getOutBuilder().getRefResolver();

        StringBuilder sb = new StringBuilder();
        int start = 0;
        while (m.find()) {

            String refTarget = m.group(2);
            String refText = m.group(4).trim();

            Ref ref = null;
            if (!UtString.empty(refTarget)) {
                ref = refResolver.resolveRef(refTarget, outFile);
            }

            sb.append(html, start, m.start());
            if (ref == null) {
                if (refResolver.isRefLocal(refTarget)) {
                    MDocLogger.getInst().warn(outFile, "Не распознана ссылка [" + refTarget + "]");
                }
                if (UtString.empty(refText)) {
                    refText = refTarget;
                }
                sb.append(m.group(1));
                sb.append(refTarget);
                sb.append(m.group(3));
                sb.append(refText);
                sb.append(m.group(5));
            } else {
                if (UtString.empty(refText)) {
                    refText = ref.getText();
                }
                sb.append(m.group(1));
                sb.append(ref.getRef());
                sb.append(m.group(3));
                sb.append(refText);
                sb.append(m.group(5));
            }

            start = m.end();
        }

        if (start == 0) {
            return html;
        }

        sb.append(html.substring(start));

        return sb.toString();
    }

}
