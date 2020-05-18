package jandcode.core.jsa.jc.impl;

import jandcode.core.jsa.jc.*;
import jandcode.jc.*;

import java.util.*;

/**
 * Простой провайдер по стандарту jc - каталог с библиотеками lib
 */
public class NodeJsLibProviderStd extends BaseNodeJsLibProvider {

    private Ctx ctx;

    public NodeJsLibProviderStd(Ctx ctx, String path) {
        this.ctx = ctx;
        this.path = path;
    }

    protected void doFillLibs(NodeJsLibList libs) {
        NodeJsLibDirLoader loader = new NodeJsLibDirLoader(ctx, path);
        List<NodeJsLib> tmpLst = loader.load();
        libs.addAll(tmpLst);
    }

}
