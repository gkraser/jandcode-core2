package jandcode.web.filter;

import jandcode.web.*;

public class Filter1 extends BaseFilter {
    public void execFilter(FilterType type, Request request) throws Exception {
        System.out.println("filter " + getName() + " " + type + " exc:" + request.getException());
    }
}
