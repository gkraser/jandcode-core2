package jandcode.core.dao.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.core.dao.*;

import java.util.*;

public class DaoManagerImpl extends BaseComp implements DaoManager {

    private BeanFactory beanFactory = new DefaultBeanFactory(this);
    private DaoProxyFactory daoProxyFactory = new DaoProxyFactory(this);
    private DaoClassDefFactory daoClassDefFactory = new DaoClassDefFactory();
    private List<DaoFilter> daoFilters = new ArrayList<>();

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);

        // регистрируем все бины в отсортированном виде
        List<Conf> filtersConf = UtConf.sortByWeight(cfg.getConf().getConfs("filter"));
        for (Conf fc : filtersConf) {
            DaoFilter filter = (DaoFilter) getBeanFactory().create(fc);
            this.daoFilters.add(filter);
        }

    }

    public Object invokeMethod(DaoMethodDef method, Object... args) throws Exception {

        // создаем контекст
        DaoContext context = createDaoContext();
        context.getBeanFactory().setParentBeanFactory(getBeanFactory());

        // создаем экземпляр dao
        Object daoInst = context.create(method.getCls());

        // создаем параметр для филтров
        DaoFilterParamsImpl filterParams = new DaoFilterParamsImpl(context, daoInst);

        try {
            // сначала все фильтры before
            for (int i = 0; i < this.daoFilters.size(); i++) {
                this.daoFilters.get(i).beforeInvoke(filterParams);
            }

            // затем оригинальный метод
            Object res = method.getMethod().invoke(daoInst, args);
            filterParams.setResult(res);

            // затем все фильтры after, в обратном порядке
            for (int i = this.daoFilters.size() - 1; i >= 0; i--) {
                this.daoFilters.get(i).afterInvoke(filterParams);
            }

        } catch (Throwable e) {

            // при ошибке error, в обратном порядке
            for (int i = this.daoFilters.size() - 1; i >= 0; i--) {
                this.daoFilters.get(i).afterInvoke(filterParams);
            }

            throw e;
        }
        //
        return filterParams.getResult();
    }

    public <A> A createDao(Class<A> cls) {
        DaoClassDef cd = daoClassDefFactory.getDaoClassDef(cls);
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
        if (inst instanceof IDaoManagerLinkSet) {
            ((IDaoManagerLinkSet) inst).setDaoManager(this);
        }
    }

    public List<DaoFilter> getDaoFilters() {
        return daoFilters;
    }

}
