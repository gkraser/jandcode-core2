package jandcode.mdoc.gsp;

import jandcode.commons.collect.*;
import jandcode.commons.error.*;
import jandcode.commons.groovy.*;
import jandcode.commons.variant.*;
import jandcode.mdoc.groovy.*;

public abstract class BaseGspTemplateContext implements IGspTemplate {

    private GroovyFactory factory;
    private StackList<BaseGspTemplate> templatesStack = new StackList<>();
    private IVariantMap args = new VariantMap();

    public void setFactory(GroovyFactory factory) {
        this.factory = factory;
    }

    ////// gsp

    protected void pushTemplate(BaseGspTemplate t) {
        templatesStack.push(t);
    }

    protected BaseGspTemplate popTemplate() {
        return templatesStack.pop();
    }

    protected BaseGspTemplate curTemplate() {
        return templatesStack.last();
    }

    /**
     * Сгенерировать по шаблону
     *
     * @param path виртуальны путь шаблона
     * @return сгенерированный текст
     */
    public String generate(String path) {
        BaseGspTemplate tml = null;
        String res = null;
        try {
            tml = this.factory.createTemplate(path);
            tml.setContext(this);
            pushTemplate(tml);
            try {
                res = tml.generate();
            } finally {
                popTemplate();
            }
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
        return res;
    }

    ////// IGspTemplate

    public void out(Object s) throws Exception {
        curTemplate().out(s);
    }

    //////

    /**
     * Аргументы контекста.
     * Можно рассматривать как глобальные переменные процесса рендеринга.
     */
    public IVariantMap getArgs() {
        return args;
    }

}
