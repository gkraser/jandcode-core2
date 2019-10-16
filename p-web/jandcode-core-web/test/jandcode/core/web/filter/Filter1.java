package jandcode.core.web.filter;

import jandcode.core.web.*;

public class Filter1 extends BaseFilter {
    public void execFilter(FilterType type, Request request) throws Exception {
        System.out.println("filter " + getName() + " " + type + " exc:" + request.getException());
    }
}
