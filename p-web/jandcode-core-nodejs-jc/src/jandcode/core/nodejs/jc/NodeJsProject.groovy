package jandcode.core.nodejs.jc


import jandcode.jc.*
import jandcode.core.nodejs.jc.impl.*

/**
 * Проект с поддержкой nodejs.
 */
class NodeJsProject extends ProjectScript {

    protected void onInclude() throws Exception {
        include(NodeJsShowlibs)
        include(NodeJsShowdeps)
        include(NodeJsGenIdea)
    }

    /**
     * Список путей/масок, которые содержат модули.
     * Только для чтения.
     */
    List<String> modulesPaths = []

    private List<NodeJsModuleProvider> _moduleProviders = null

    /**
     * Модули nodejs, которые содержатся в проекте.
     * Параметром может быть каталог, маска.
     */
    void modules(String... modules) {
        this.modulesPaths.addAll(modules)
    }

    /**
     * Провайдеры модулей этого проекта
     */
    List<NodeJsModuleProvider> getModuleProviders() {
        if (_moduleProviders == null) {
            List<NodeJsModuleProvider> tmp = new ArrayList<>()
            for (path in modulesPaths) {
                NodeJsModuleProvider prv = new NodeJsModuleProviderPath(ctx, wd(path), this)
                tmp.add(prv)
            }
            _moduleProviders = tmp
        }
        return _moduleProviders
    }

}
