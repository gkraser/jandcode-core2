package jandcode.web.gsp.impl;

import jandcode.commons.*;
import jandcode.commons.collect.*;
import jandcode.commons.error.*;
import jandcode.commons.impl.*;
import jandcode.commons.variant.*;
import jandcode.core.*;
import jandcode.web.*;
import jandcode.web.gsp.*;
import jandcode.web.virtfile.*;

import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * Реализация GspContext для процесса рендеринга Gsp.
 */
public class GspContextImpl implements ITextBuffer, GspContext {

    public static Pattern P_LATER = Pattern.compile("\\~\\{([A-Z0-9]{32})\\}\\~");

    protected TextBufferImpl curTextBuffer;
    protected StackList<TextBufferImpl> stackTextBuffer = new StackList<>();
    private GspPool gspPool = new GspPool();
    protected StackList<Gsp> stackGsp = new StackList<>();

    // context
    private App app;
    private WebService webService;
    private VariantMap args = new VariantMap();
    private StackList<IGspFactory> gspFactoryStack = new StackList<>();
    private boolean renderWorking;
    private List<IGspContextSubstVar> substVarHandlers;
    private BeanFactory beanFactory = new DefaultBeanFactory(this);
    private Map<String, LaterItem> laterItems = new HashMap<>();

    class GspPool extends StackedPool<Gsp> {
        protected Gsp createInstance(String name) throws Exception {
            GspImpl gsp = (GspImpl) createGsp(name);
            return gsp;
        }
    }

    class DummyGsp extends BaseGsp {
        protected void onRender() throws Exception {
        }
    }

    class LaterItem {
        IGspLaterHandler handler;
        String key;

        public LaterItem(IGspLaterHandler handler) {
            this.handler = handler;
            this.key = UtString.md5Str("q" + laterItems.size());
        }

        public void out(ITextBuffer b) {
            b.out("~{");
            b.out(key);
            b.out("}~");
        }

    }

    //////

    public GspContextImpl(App app) {
        this.app = app;
        this.webService = app.bean(WebService.class);

        // фиктивная вершина стека
        GspImpl dummyRoot = new DummyGsp();
        dummyRoot.setApp(app);
        dummyRoot.bindContext(this);
        stackGsp.push(dummyRoot);

        //
        pushBuffer();
    }

    //////

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void beanInit(Object inst) {
        if (inst instanceof IGspContextLinkSet) {
            ((IGspContextLinkSet) inst).setGspContext(GspContextImpl.this);
        }
    }

    //////

    public void outGsp(String gspName, Map gspArgs, Object body) throws Exception {
        Gsp ownerGsp = stackGsp.last();
        Gsp gsp = gspPool.getObject(gspName);
        ((GspImpl) gsp).bindOwner(ownerGsp);
        if (!gsp.isRemoveFromPool()) {
            // если gsp в пуле, чистим аргументы
            gsp.getArgs().clear();
        }
        if (gspArgs != null) {
            gsp.getArgs().putAll(gspArgs);
        }
        if (body != null) {
            gsp.getArgs().put(Gsp.ARG_BODY, body);
        }

        try {
            outGsp(gsp);
        } finally {
            gspPool.returnObject(gspName, gsp.isRemoveFromPool());
        }

    }

    public void outGsp(Gsp gsp) throws Exception {
        if (gsp.getOwner() == null) {
            ((GspImpl) gsp).bindOwner(stackGsp.last());
        }
        stackGsp.push(gsp);
        try {
            gsp.renderTo(this);
        } finally {
            stackGsp.pop();
        }
    }

    public void includeGsp(Gsp to, String gspName) throws Exception {
        GspImpl gsp = (GspImpl) gspPool.getObject(gspName);
        gsp.bindOwner(to.getOwner());
        gsp.bindArgs(to.getArgs());
        stackGsp.push(gsp);
        try {
            gsp.renderTo(this);
        } finally {
            stackGsp.pop();
            gspPool.returnObject(gspName, gsp.isRemoveFromPool());
        }
    }

    //////

    public void writeTo(Writer w) throws Exception {
        stackTextBuffer.first().writeTo(w);
    }

    public String toString() {
        return stackTextBuffer.first().toString();
    }

    public ITextBuffer out(Object s) {
        if (s instanceof IGspLaterHandler) {
            LaterItem it = new LaterItem((IGspLaterHandler) s);
            laterItems.put(it.key, it);
            it.out(curTextBuffer);
            return curTextBuffer;

        } else if (s instanceof IGspRender) {
            try {
                ((IGspRender) s).renderTo(this);
                return curBuffer();
            } catch (Exception e) {
                throw new XErrorWrap(e);
            }
        } else {
            return curTextBuffer.out(s);
        }
    }

    public boolean isEmpty() {
        return stackTextBuffer.first().isEmpty();
    }

    public boolean isWhite() {
        return stackTextBuffer.first().isWhite();
    }

    //////

    public ITextBuffer pushBuffer() {
        TextBufferImpl b = new TextBufferImpl(null);
        stackTextBuffer.push(b);
        curTextBuffer = b;
        return b;
    }

    public ITextBuffer popBuffer() {
        if (stackTextBuffer.size() == 1) {
            throw new XError("popBuffer без pushBuffer");
        }
        TextBufferImpl b = stackTextBuffer.pop();
        curTextBuffer = stackTextBuffer.last();
        return b;
    }

    public ITextBuffer curBuffer() {
        return curTextBuffer;
    }

    private String normalizeLayerName(String name) {
        return UtVDir.normalize(name);
    }

    //////

    /**
     * Метод вызывается после окончания рендеринга корневого gsp,
     * который собственно и инициировал вывод.
     * <p>
     * Если в стеке остались буферы, они извлекаются из него.
     */
    public void postOut(Gsp gsp) {
        while (stackTextBuffer.size() > 1) {
            popBuffer();
        }
    }

    ////// context

    public ITextBuffer render(Gsp gsp) throws Exception {
        ((GspImpl) gsp).bindContext(this);
        outGsp(gsp);
        postOut(gsp);
        return this;
    }

    public ITextBuffer render(String gspName, Map args) throws Exception {
        if (renderWorking) {
            throw new XError("Вызов render для GspContext не может быть вложенным");
        }
        renderWorking = true;
        try {
            stackTextBuffer.clear();
            laterItems.clear();
            //
            pushBuffer();
            //
            Gsp gsp = createGsp(gspName);
            if (args != null) {
                gsp.getArgs().putAll(args);
            }
            outGsp(gsp);
            postOut(gsp);

            // later
            if (laterItems.size() > 0) {
                TextBufferImpl tmpBuf = new TextBufferImpl();
                renderLater(curTextBuffer, tmpBuf);
                stackTextBuffer.set(0, tmpBuf);
            }

            //
            return stackTextBuffer.first();
        } finally {
            renderWorking = false;
        }
    }

    private void renderLater(ITextBuffer bufSrc, ITextBuffer bufDest) {
        String src = bufSrc.toString();
        Matcher m = P_LATER.matcher(src);

        int start = 0;
        while (m.find()) {
            bufDest.out(src.substring(start, m.start()));

            String key = m.group(1);
            LaterItem it = laterItems.get(key);
            if (it != null) {
                ITextBuffer tmp = pushBuffer();
                it.handler.outLater(stackGsp.get(0)); // dummy!
                popBuffer();
                bufDest.out(tmp);
            }

            start = m.end();
        }
        if (start == 0) {
            bufDest.out(bufSrc);
        } else {
            bufDest.out(src.substring(start));
        }
    }

    public ITextBuffer render(String gspName) throws Exception {
        return render(gspName, null);
    }

    public IVariantMap getArgs() {
        return args;
    }

    public void pushGspFactory(IGspFactory factory) {
        gspFactoryStack.push(factory);
    }

    public void popGspFactory() {
        gspFactoryStack.pop();
    }

    public Gsp createGsp(String gspName) throws Exception {
        Gsp gsp = null;
        if (gspFactoryStack.size() > 0) {
            for (int i = gspFactoryStack.size() - 1; i >= 0; i--) {
                IGspFactory f = gspFactoryStack.get(i);
                gsp = f.createGsp(gspName);
                if (gsp != null) {
                    break;
                }
            }
        }
        if (gsp == null) {
            gsp = webService.createGsp(gspName);
        }
        ((GspImpl) gsp).bindContext(this);
        return gsp;
    }

    public VirtFile findFile(String path) {
        return webService.findFile(path);
    }

    public BaseGsp getCurrentGsp() {
        return (BaseGsp) stackGsp.last();
    }

    public BaseGsp getRootGsp() {
        if (stackGsp.size() > 1) {
            return (BaseGsp) stackGsp.get(1);
        } else {
            throw new XError("Нет корневой gsp");
        }
    }

    //////

    class GspContextSubstVarParser extends SubstVarParser {
        public GspContextSubstVarParser() {
            startVar1 = '#';
        }

        public String onSubstVar(String v) {
            for (int i = 0; i < substVarHandlers.size(); i++) {
                IGspContextSubstVar svh = substVarHandlers.get(i);
                String a = svh.handleSubstVar(v);
                if (a != null) {
                    return a;
                }
            }
            throw new XError("Неизвестная пременная подстановки [{0}]");
        }
    }

    private GspContextSubstVarParser substVarParser;

    public String substVar(String s) {
        if (s == null) {
            return "";
        }
        if (substVarHandlers == null || s.indexOf('#') == -1) {
            return s;
        }
        if (substVarParser == null) {
            substVarParser = new GspContextSubstVarParser();
        }
        substVarParser.loadFrom(s);
        return substVarParser.getResult();
    }

    public void addSubstVarHandler(IGspContextSubstVar handler) {
        if (substVarHandlers == null) {
            substVarHandlers = new ArrayList<>();
        }
        substVarHandlers.add(0, handler);
    }

    //////

}

