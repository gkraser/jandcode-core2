package jandcode.web.gsp;

import jandcode.commons.*;
import jandcode.web.*;
import jandcode.web.render.*;

import java.util.*;

/**
 * Шаблон gsp для использования в качестве ответа сервера.
 * <pre>{@code
 * request.render(new GspTemplate('template',[a:1]))
 * }</pre>
 */
public class GspTemplate implements IRender {

    private String gspName;
    private Map args;
    private String contentType = "text/html";

    public GspTemplate(String gspName) {
        this.gspName = gspName;
    }

    public GspTemplate(String gspName, Map args) {
        this.gspName = gspName;
        this.args = args;
    }

    public GspTemplate(String gspName, Map args, String contentType) {
        this.gspName = gspName;
        this.args = args;
        this.contentType = contentType;
    }

    public String getGspName() {
        return gspName;
    }

    public void setGspName(String gspName) {
        this.gspName = gspName;
    }

    public Map getArgs() {
        return args;
    }

    public void setArgs(Map args) {
        this.args = args;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    //////

    public void render(Object data, Request request) throws Exception {
        WebService gsvc = request.getApp().bean(WebService.class);
        GspContext ctx = gsvc.createGspContext();
        ITextBuffer buf = ctx.render(getGspName(), getArgs());

        //todo prepare textBuffer for js/css???

        if (!request.isContentTypeAssigned() && !UtString.empty(getContentType())) {
            request.setContentType(getContentType());
        }

        // пишем
        buf.writeTo(request.getOutWriter());
    }

}
