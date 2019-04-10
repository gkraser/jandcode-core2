package jandcode.web;

import jandcode.web.impl.*;

/**
 * Обертка для redirect (sendRedirect).
 * Этот exception генерируется в методах {@link RequestImpl#redirect(java.lang.String)}
 * и аналогичных. Можно использовать непосредственно.
 */
public class HttpRedirect extends RuntimeException {

    private String url;

    public HttpRedirect(String url) {
        super("");
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}
