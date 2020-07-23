package jandcode.core.web.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.groovy.*;
import jandcode.commons.io.*;
import jandcode.commons.named.*;
import jandcode.core.*;
import jandcode.core.web.*;
import jandcode.core.web.action.*;
import jandcode.core.web.filter.*;
import jandcode.core.web.gsp.*;
import jandcode.core.web.logger.*;
import jandcode.core.web.render.*;
import jandcode.core.web.type.*;
import jandcode.core.web.virtfile.FileType;
import jandcode.core.web.virtfile.*;
import org.apache.commons.vfs2.*;
import org.slf4j.*;

import javax.servlet.http.*;
import java.util.*;

public class WebServiceImpl extends BaseComp implements WebService {

    protected static Logger log = LoggerFactory.getLogger(WebService.class);

    protected HttpServlet httpServlet;
    protected ThreadLocal<Request> requestThreadLocal = new ThreadLocal<>();
    protected RequestLogger requestLogger;
    protected Request requestDummy;


    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        Conf conf = cfg.getConf();
        //

        String s;

        // logger
        if (getApp().getEnv().isDev()) {
            s = "dev";
        } else {
            s = "production";
        }
        s = conf.getString("requestLogger/" + s + "/class", "jandcode.core.web.logger.RequestLoggerDefault");
        requestLogger = (RequestLogger) getApp().create(s);

    }

    //////

    public HttpServlet getHttpServlet() {
        return httpServlet;
    }

    public void setHttpServlet(HttpServlet httpServlet) {
        this.httpServlet = httpServlet;
        this.requestDummy = createRequestDummy();
    }

    public Request getRequest() {
        Request res = requestThreadLocal.get();
        if (res == null) {
            return requestDummy;
        }
        return res;
    }

    /**
     * Установить текущий запрос. Вызывается из тестов.
     */
    public void setRequest(Request request) {
        requestThreadLocal.set(request);
    }

    ////// factorys

    protected Request createRequest(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        return new RequestImpl(getApp(), httpRequest, httpResponse);
    }

    protected Request createRequestDummy() {
        return new RequestDummyImpl(getApp(), null, null);
    }

    protected void execFilter(FilterType type, Request request) throws Exception {
        getFilterService().execFilter(type, request);
    }

    ////// actions

    protected ActionService getActionService() {
        return getApp().bean(ActionService.class);
    }

    public NamedList<ActionDef> getActions() {
        return getActionService().getActions();
    }

    public IAction createAction(Request request) {
        return getActionService().createAction(request);
    }

    public IAction createAction(String name) {
        return getActionService().createAction(name);
    }

    public Iterable<IActionFactory> getActionFactorys() {
        return getActionService().getActionFactorys();
    }

    ////// renders

    protected RenderService getRenderService() {
        return getApp().bean(RenderService.class);
    }

    public NamedList<RenderDef> getRenders() {
        return getRenderService().getRenders();
    }

    public IRender createRender(Object data, Request request) {
        return getRenderService().createRender(data, request);
    }

    public IRender createRender(String name) {
        return getRenderService().createRender(name);
    }

    ////// types

    protected TypeService getTypeService() {
        return getApp().bean(TypeService.class);
    }

    public NamedList<TypeDef> getTypes() {
        return getTypeService().getTypes();
    }

    public TypeDef findType(Class cls) {
        return getTypeService().findType(cls);
    }

    ////// files

    protected VirtFileService getVirtFileService() {
        return getApp().bean(VirtFileService.class);
    }

    public List<jandcode.core.web.virtfile.FileType> getFileTypes() {
        return getVirtFileService().getFileTypes();
    }

    public FileType findFileType(String name) {
        return getVirtFileService().findFileType(name);
    }

    public VirtFile wrapFile(String realFileName) {
        return getVirtFileService().wrapFile(realFileName);
    }

    public VirtFile wrapFile(FileObject realFile) {
        return getVirtFileService().wrapFile(realFile);
    }

    public List<Mount> getMounts() {
        return getVirtFileService().getMounts();
    }

    public List<VirtFile> findFiles(String path) {
        return getVirtFileService().findFiles(path);
    }

    public VirtFile findFile(String path) {
        return getVirtFileService().findFile(path);
    }

    public DirScanner<VirtFile> createDirScanner(String dir) {
        return getVirtFileService().createDirScanner(dir);
    }

    public VirtFile getFile(String path) {
        return getVirtFileService().getFile(path);
    }

    ////// gsp


    protected GspService getGspService() {
        return getApp().bean(GspService.class);
    }

    public NamedList<GspDef> getGsps() {
        return getGspService().getGsps();
    }

    public String getGspPath(String gspName) {
        return getGspService().getGspPath(gspName);
    }

    public String getGspSourceText(String gspName) {
        return getGspService().getGspSourceText(gspName);
    }

    public String getGspClassText(String gspName) {
        return getGspService().getGspClassText(gspName);
    }

    public Gsp createGsp(String gspName) throws Exception {
        return getGspService().createGsp(gspName);
    }

    public GroovyClazz compileGsp(FileObject gspFile) throws Exception {
        return getGspService().compileGsp(gspFile);
    }

    public GspContext createGspContext() {
        return getGspService().createGspContext();
    }

    ////// filters

    protected FilterService getFilterService() {
        return getApp().bean(FilterService.class);
    }

    public NamedList<FilterDef> getFilters() {
        return getFilterService().getFilters();
    }

    //////

    public void handleRequest(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws Throwable {
        // кодировка запроса
        httpRequest.setCharacterEncoding("UTF-8");

        // создаем новый запрос
        Request request = createRequest(httpRequest, httpResponse);

        // делаем его текущим
        handleRequest(request);
    }

    /**
     * Установить готовность запроса к рендерингу
     */
    protected void renderReady(Request request) {
        if (request instanceof RequestImpl) {
            ((RequestImpl) request).setRenderReady(true);
        }
    }

    /**
     * Возвращает true для кодов ошибок http, для которых не нужно делать
     * реальный sendError, достаточно установить статус в текущем запросе.
     *
     * @param code проверяемый код
     */
    protected boolean isNotSendErrorCode(int code) {
        return code == 304;
    }

    public void handleRequest(Request request) throws Throwable {

        // делаем его текущим
        setRequest(request);

        // логгируем начало
        requestLogger.logStart(request);

        // уведомляем о начале выполнения
        execFilter(FilterType.startRequest, request);

        boolean terminated = false;

        try {
            IAction a = createAction(request);
            if (a == null) {
                throw new HttpError(404, "Action not found");
            }

            request.setAction(a);

            // уведомляем о начале выполнения action
            execFilter(FilterType.beforeAction, request);

            // вызываем именно так, т.к. action может быть и сменена
            request.getAction().exec(request);

            // уведомляем о конце выполнения action
            execFilter(FilterType.afterAction, request);

            Object data = request.getRenderData();
            if (data == null) {
                throw new XError("responseData not set");
            }

            IRender ren = createRender(data, request);

            if (ren == null) {
                throw new XError("Render not found for class {0}", data.getClass());
            }

            request.setRender(ren);

            // уведомляем о начале выполнения render
            execFilter(FilterType.beforeRender, request);

            // запрос готов рендерингу, теперь у него можно брать outWriter или outStream
            renderReady(request);

            // вызываем именно так, т.к. render может быть и сменен
            request.getRender().render(data, request);

            // уведомляем о конце выполнения render
            execFilter(FilterType.afterRender, request);

            // если никто не запрашивал поток вывода - это странно...
            if (!request.isOutGet()) {
                throw new XError("out stream not getted, bad render");
            }

        } catch (Throwable e) {

            terminated = true; // запрос был прерван

            // выявляем реальную ошибку
            Throwable e1 = UtError.getErrorConvertor().getReal(e);
            if (e1 == null) {
                e1 = e;
            }

            // какая ошибка была
            request.setException(e1);

            if (e1 instanceof HttpRedirect) {
                HttpRedirect e2 = (HttpRedirect) e1;
                requestLogger.logHttpRedirect(request, e2);
                request.getHttpResponse().sendRedirect(e2.getUrl());

            } else if (e1 instanceof HttpError) {
                HttpError e2 = (HttpError) e1;
                requestLogger.logHttpError(request, e2);

                // для части ошибок не генерим sendError, а просто ставим статус
                boolean needSendError = true;
                if (isNotSendErrorCode(e2.getCode())) {
                    if (!request.isOutGet()) {
                        needSendError = false;
                        request.getHttpResponse().setStatus(e2.getCode());
                    }
                }

                if (needSendError) {
                    // sendError с сообщением и без могут иметь разную реализацию
                    if (UtString.empty(e.getMessage())) {
                        request.getHttpResponse().sendError(e2.getCode());
                    } else {
                        request.getHttpResponse().sendError(e2.getCode(), e.getMessage());
                    }
                }

            } else {
                requestLogger.logError(request, e1);
                throw e1;

            }

        } finally {

            // логируем, если была, скрытую ошибку
            if (!terminated && request.getException() != null) {
                requestLogger.logError(request, request.getException());
            }

            // уведомляем о конце выполнения
            execFilter(FilterType.stopRequest, request);

            requestLogger.logStop(request);

            // все, он не нужен
            setRequest(null);
        }

    }

}
