package jandcode.core.nodejs.jc.impl;

import jandcode.commons.named.*;
import jandcode.commons.stopwatch.*;
import jandcode.jc.*;
import jandcode.core.nodejs.jc.*;

/**
 * Простой провайдер по стандарту jc - файл package.json, каталог с package.json
 * или каталог в формате node_modules.
 */
public class NodeJsModuleProviderPath extends BaseNodeJsModuleProvider {

    private Ctx ctx;
    private Project ownerProject;

    public NodeJsModuleProviderPath(Ctx ctx, String path, Project ownerProject) {
        this.ctx = ctx;
        this.path = path;
        this.ownerProject = ownerProject;
    }

    public NodeJsModuleProviderPath(Ctx ctx, String path) {
        this(ctx, path, null);
    }

    protected void doFillLibs(NamedList<NodeJsModule> modules) {
        NodeJsModuleLoader ldr = new NodeJsModuleLoader(this.ctx, this.path, this.ownerProject);
        NamedList<NodeJsModule> tmp = ldr.load();
        modules.addAll(tmp);
    }

}
