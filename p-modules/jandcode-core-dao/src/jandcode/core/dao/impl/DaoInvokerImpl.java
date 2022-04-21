package jandcode.core.dao.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.dao.*;
import org.slf4j.*;

import java.util.*;

public class DaoInvokerImpl extends BaseComp implements DaoInvoker {

    protected static Logger log = LoggerFactory.getLogger(DaoInvoker.class);

    private BeanFactory beanFactory = new DefaultBeanFactory();
    private DaoProxyFactory daoProxyFactory = new DaoProxyFactory(this);
    private List<DaoFilter> daoFilters = new ArrayList<>();

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);

        //
        getBeanFactory().beanConfigure(cfg);

        // регистрируем все фильтры в отсортированном виде
        List<Conf> filtersConf = UtConf.sortByWeight(cfg.getConf().getConfs("filter"));
        for (Conf fc : filtersConf) {
            DaoFilter filter = (DaoFilter) getBeanFactory().create(fc);
            this.daoFilters.add(filter);
        }

    }

    public DaoContext invokeDao(DaoContextIniter ctxIniter, DaoMethodDef method, Object... args) throws Exception {

        DaoService daoService = getApp().bean(DaoService.class);
        DaoLogger daoLogger = daoService.getDaoLogger();

        // создаем контекст
        DaoContextImpl context = new DaoContextImpl(this, method);
        context.getBeanFactory().setParentBeanFactory(getBeanFactory());

        // создаем экземпляр dao
        Object daoInst = context.create(method.getClassDef().getClsInst());
        context.setDaoInst(daoInst);

        daoLogger.logStart(context);

        // формируем фильтры
        List<DaoFilter> filters = new ArrayList<>(this.daoFilters);
        if (daoInst instanceof DaoFilter) {
            filters.add((DaoFilter) daoInst);
        }

        try {
            // инициализация контекста
            if (ctxIniter != null) {
                ctxIniter.initDaoContext(context);
            }

            // сначала все фильтры before
            for (int i = 0; i < filters.size(); i++) {
                filters.get(i).execDaoFilter(DaoFilterType.before, context);
            }

            // затем оригинальный метод
            Object res = method.getMethod().invoke(daoInst, args);
            context.setResult(res);

            // затем все фильтры after, в обратном порядке
            for (int i = filters.size() - 1; i >= 0; i--) {
                filters.get(i).execDaoFilter(DaoFilterType.after, context);
            }

            daoLogger.logStop(context);

        } catch (Throwable e) {

            // запоминаем ошибку
            context.setException(e);

            // выкидываем ее
            throw e;

        } finally {
            // ошибки внутри фильтров игнорируем и логгируем
            for (int i = 0; i < filters.size(); i++) {
                try {
                    filters.get(i).execDaoFilter(DaoFilterType.cleanup, context);
                } catch (Exception ex) {
                    if (log.isErrorEnabled()) {
                        log.error("Error in dao-cleanup filter " + filters.get(i).getClass().getName(), ex);
                    }
                }
            }
        }
        //
        return context;
    }

    public Object invokeDao(DaoMethodDef method, Object... args) throws Exception {
        DaoContext ctx = invokeDao(null, method, args);
        return ctx.getResult();
    }

    public <A> A createDao(Class<A> cls) {
        DaoClassDef cd = getApp().bean(DaoService.class).getDaoClassDef(cls);
        try {
            return (A) daoProxyFactory.createDaoProxyInst(cd);
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public List<DaoFilter> getDaoFilters() {
        return daoFilters;
    }

    public DaoClassDef getDaoClassDef(Class cls) {
        return getApp().bean(DaoService.class).getDaoClassDef(cls);
    }

}
