package jandcode.commons.conf;

/**
 * Предок для плагинов загрузки Conf
 */
public abstract class BaseConfLoaderPlugin implements ConfLoaderPlugin {

    private ConfLoaderContext loader;

    public void initPlugin(ConfLoaderContext loader) throws Exception {
        this.loader = loader;
    }

    /**
     * Ссылка на loader
     */
    protected ConfLoaderContext getLoader() {
        return loader;
    }

    /**
     * Ссылка на загружаемую конфигурацию
     */
    protected Conf getRoot() {
        return loader.getRoot();
    }

    public String getVar(String var) throws Exception {
        return null;
    }

    public boolean execFunc(String funcName, Conf params, Conf context) throws Exception {
        return false;
    }

    public void afterLoad() throws Exception {
    }

    public Object evalExpression(Conf expr) {
        return null;
    }

}
