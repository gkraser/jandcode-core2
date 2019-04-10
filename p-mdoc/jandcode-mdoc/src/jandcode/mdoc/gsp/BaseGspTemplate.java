package jandcode.mdoc.gsp;

import jandcode.commons.groovy.*;

/**
 * Предок для шаблонов страниц.
 * В этот класс компилируются .gsp
 */
public abstract class BaseGspTemplate implements IGspTemplate {

    private StringBuilder sb = new StringBuilder();

    private BaseGspTemplateContext context;

    /**
     * Контекст генерации
     */
    public BaseGspTemplateContext getContext() {
        return context;
    }

    public void setContext(BaseGspTemplateContext context) {
        this.context = context;
    }

    /**
     * Метод генерации
     */
    public String generate() {
        sb = new StringBuilder();
        onGenerate();
        return sb.toString();
    }

    /**
     * Реализация процесса генерации
     */
    protected abstract void onGenerate();

    //////

    public void out(Object s) throws Exception {
        if (s == null) {
            return;
        }
        sb.append(s);
    }

}
