package jandcode.mdoc.builder;

import jandcode.core.*;
import jandcode.mdoc.*;
import jandcode.mdoc.groovy.*;
import jandcode.mdoc.gsp.*;
import jandcode.mdoc.impl.*;
import jandcode.mdoc.source.*;
import jandcode.mdoc.topic.*;
import jandcode.mdoc.topic.impl.*;

/**
 * Предок для всех builder.
 * Является оберткой вокруг Doc и имеет собственную копию статей и файлов из Doc.
 */
public abstract class BaseOutBuilder implements OutBuilder, IBeanIniter {

    private Toc toc;
    private Doc doc;
    private Doc originalDoc;
    private OutFileHolder outFiles = new OutFileHolder();
    private BeanFactory beanFactory = new DefaultBeanFactory(this);
    private RefResolver refResolver;

    /**
     * Конфигурирование builder.
     * Автоматически вызывается из OutBuilderService.createBuilder
     *
     * @param doc документ
     */
    public void configure(Doc doc) throws Exception {
        this.refResolver = create(RefResolver.class);
        this.originalDoc = doc;
        this.doc = new DocWrap(this.originalDoc);

        // бины
        BeanFactory bf = getBeanFactory();

        bf.registerBean(GroovyFactory.class.getName(), GroovyFactory.class);
    }

    public void build() throws Exception {
        // обязательная инициализация
        onBeforeBuild();
        // сборка
        onBuild();
    }

    protected void onBeforeBuild() throws Exception {
        getOutFiles().clear();
        this.toc = new TocImpl();
    }

    /**
     * Реализация build
     */
    protected abstract void onBuild() throws Exception;

    //////

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void beanInit(Object inst) {
        if (inst instanceof IOutBuilderLinkSet) {
            ((IOutBuilderLinkSet) inst).setOutBuilder(this);
        }
    }

    public App getApp() {
        return getDoc().getApp();
    }

    public Doc getDoc() {
        return doc;
    }

    public Doc getOriginalDoc() {
        return originalDoc;
    }

    public OutFileHolder getOutFiles() {
        return outFiles;
    }

    public Toc getToc() {
        return toc;
    }

    public void setToc(Toc toc) {
        this.toc = toc;
    }

    public RefResolver getRefResolver() {
        return refResolver;
    }

    public DocMode getMode() {
        return getDoc().getMode();
    }

    public void outTo(OutDir outDir) throws Exception {
        // выводим все
        for (OutFile f : getOutFiles().getItems()) {
            if (f.isNeed()) {
                outDir.outFile(f);
            }
        }
    }

    public GspTemplateContext createGspTemplateContext(OutFile outFile) {
        return new GspTemplateContext(bean(GroovyFactory.class), outFile, this);
    }

    public boolean hasChangedSinceTime(OutFile outFile, long time) {
        if (outFile == null) {
            return false;
        }
        for (String p : outFile.getDependFiles()) {
            SourceFile f = getDoc().getSourceFiles().find(p);
            if (f != null) {
                if (f.getLastModTime() > time) {
                    return true;
                }
            }
        }
        return false;
    }

}
