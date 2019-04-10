package jandcode.mdoc.html;

import jandcode.commons.*;
import jandcode.mdoc.*;
import jandcode.mdoc.builder.*;

import java.util.regex.*;

/**
 * Замена маркеров url в тексте html на реальные ссылки.
 * Этот post processor выполняется последним, непосредственно перед выводом
 * файла, так как ему нужна полностью свормированная среда в builder.
 */
public class UrlRewriterPostProcessor extends BaseOutBuilderMember implements HtmlPostProcessor {

    // для таких маркеров: {~ref~:_theme/lib/normalize/normalize.css}
    private static Pattern refPattern = Pattern.compile("(\\{~ref~:)(.*?)(\\})");

    public String process(String html, OutFile outFile) {
        String curFileDir = UtFile.path(outFile.getPath());

        //
        Matcher m = refPattern.matcher(html);

        StringBuilder sb = new StringBuilder();
        int start = 0;
        while (m.find()) {
            sb.append(html, start, m.start());

            String ref = resolve(m.group(2), outFile, curFileDir);
            sb.append(ref);

            start = m.end();
        }
        if (start == 0) {
            return html;
        }

        sb.append(html.substring(start));

        return sb.toString();
    }

    public String resolve(String refOrig, OutFile outFile, String curFileDir) {
        String ref = refOrig;

        if (UtString.empty(ref)) {
            ref = "index";
        }

        int a = ref.indexOf('#');
        String section = null;
        if (a != -1) {
            section = ref.substring(a + 1);
            ref = ref.substring(0, a);
        }

        OutFile f = null;

        //
        String ext = UtFile.ext(ref);
        if (UtString.empty(ext)) {
            // нет расширения, значит это id-статьи
            f = getOutBuilder().getOutFiles().findByTopicId(ref);
        } else {
            // расширение есть - значит файл
            f = getOutBuilder().getOutFiles().findBySourcePath(ref);
        }
        if (f == null) {
            MDocLogger.getInst().warn(outFile, "Не распознана ссылка [" + refOrig + "]");
            return "NOT_FOUND_REF/" + refOrig;
        }

        // нашли ссылку на файл, теперь этот файл нужен в выводе
        f.setNeed(true);

        //
        String s = UtVDir.getRelPath(curFileDir, f.getPath());
        if (section != null) {
            s = s + "#" + section;
        }
        return s;
    }

}
