package jandcode.core.jsa.utils;

import jandcode.commons.*;

import java.util.regex.*;

/**
 * Изменение url в css
 */
public class CssUrlRebase {

    protected static Pattern PAT_URL = Pattern.compile("(url\\s*\\()(.*?)(\\))", Pattern.CASE_INSENSITIVE);

    public String rebase(String src, String srcFolder, String destFolder, String prefix) {
        if (destFolder.equals(srcFolder)) {
            return src;
        }
        Matcher m = PAT_URL.matcher(src);

        StringBuilder sb = new StringBuilder();
        int start = 0;
        while (m.find()) {
            sb.append(src.substring(start, m.start()));

            String url = m.group(2);
            String res = rebaseUrl(url, srcFolder, destFolder, prefix);

            //
            sb.append(m.group(1));
            sb.append(res);
            sb.append(m.group(3));

            start = m.end();
        }
        if (start == 0) {
            return src;
        }

        sb.append(src.substring(start));

        return sb.toString();

    }

    private String rebaseUrl(String url, String srcFolder, String destFolder, String prefix) {
        if (UtString.empty(url)) {
            return "";
        }
        if (url.indexOf(':') != -1) {
            return url; // будем считать его абсолютным
        }
        String s = url;
        String p = null;
        char q = s.charAt(0);
        if (q == '\'' || q == '\"') {
            s = s.substring(1, s.length() - 1);
        } else {
            q = 0;
        }
        int a = s.indexOf('?');
        if (a != -1) {
            p = s.substring(a);
            s = s.substring(0, a);
        }
        if (!s.startsWith(".")) {
            s = "./" + s;
        }
        s = UtVDir.expandRelPath(srcFolder, s);
        s = prefix + UtVDir.getRelPath(destFolder, s);

        if (p != null) {
            s = s + p;
        }
        if (q != 0) {
            s = "" + q + s + q;
        }
        return s;
    }

}
