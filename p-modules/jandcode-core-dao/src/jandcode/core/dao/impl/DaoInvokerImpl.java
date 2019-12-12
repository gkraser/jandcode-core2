package jandcode.core.dao.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.dao.*;
import org.slf4j.*;

import java.util.*;

public class DaoInvokerImpl extends BaseComp implements DaoInvoker, IBeanIniter {

    protected static Logger log = LoggerFactory.getLogger(DaoInvoker.class);

    private BeanFactory beanFactory = new DefaultBeanFactory(this);
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

    public Object invokeDao(DaoMethodDef method, Object... args) throws Exception {

        DaoService daoService = getApp().bean(DaoService.class);
        DaoLogger daoLogger = daoService.getDaoLogger();

        // создаем контекст
        DaoContext context = createDaoContext();
        context.getBeanFactory().setParentBeanFactory(getBeanFactory());

        // создаем экземпляр dao
        Object daoInst = context.create(method.getCls());

        // создаем параметр для фильтров
        DaoFilterParamsImpl filterParams = new DaoFilterParamsImpl(context, daoInst, method);

        daoLogger.logStart(filterParams);
        int filterPos = 0;
        try {
            // сначала все фильтры before
            // засекаем filterPos, который выполнили последним (для обработки ошибок)
            for (int i = 0; i < this.daoFilters.size(); i++) {
                filterPos = i;
                this.daoFilters.get(i).execDaoFilter(DaoFilterType.before, filterParams);
            }

            // затем оригинальный метод
            Object res = method.getMethod().invoke(daoInst, args);
            filterParams.setResult(res);

            // затем все фильтры after, в обратном порядке
            // засекаем filterPos, который выполнили последним (для обработки ошибок)
            for (int i = this.daoFilters.size() - 1; i >= 0; i--) {
                filterPos = i;
                this.daoFilters.get(i).execDaoFilter(DaoFilterType.after, filterParams);
            }

            daoLogger.logStop(filterParams);

        } catch (Throwable e) {

            // запоминаем ошибку
            filterParams.setException(e);

            // при ошибке error, в обратном порядке
            // начиная с последнего выполненного before
            // ошибки внутри фильтров игнорируем и логгируем
            for (int i = filterPos; i >= 0; i--) {
                try {
                    this.daoFilters.get(i).execDaoFilter(DaoFilterType.error, filterParams);
                } catch (Exception ex) {
                    if (log.isErrorEnabled()) {
                        log.error("Error in dao-error filter " + this.daoFilters.get(i).getClass().getName(), e);
                    }
                }
            }

            throw e;
        }
        //
        return filterParams.getResult();
    }

    public <A> A createDao(Class<A> cls) {
        DaoClassDef cd = getApp().bean(DaoService.class).getDaoClassDef(cls);
        try {
            return (A) daoProxyFactory.createDaoProxyInst(cd);
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
    }

    private DaoContext createDaoContext() {
        return new DaoContextImpl(getApp());
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void beanInit(Object inst) {
        if (inst instanceof IDaoInvokerLinkSet) {
            ((IDaoInvokerLinkSet) inst).setDaoInvoker(this);
        }
    }

    public List<DaoFilter> getDaoFilters() {
        return daoFilters;
    }

}
