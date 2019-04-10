package jandcode.core.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.core.*;

import java.lang.reflect.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;

@SuppressWarnings("unchecked")
public class BeanFactoryImpl implements BeanFactory {

    //todo реализация конкурентного map с соблюдением порядка добавления. Тут - имитация

    protected Map<String, BeanDefImpl> items = new ConcurrentHashMap<>();
    protected List<BeanDefImpl> itemsOrdered = new CopyOnWriteArrayList<>();

    protected volatile boolean itemsDirty;
    protected BeanFactory parentBeanFactory;
    protected IBeanIniter defaultBeanIniter;

    protected class BeanDefImpl implements BeanDef {

        protected Class cls;
        protected Object inst;
        protected boolean autoCreate;
        protected boolean prototype;
        protected String name;
        protected Conf conf;
        protected volatile boolean busy;

        public BeanDefImpl(String name, Class cls, Conf conf) {
            this.name = name;
            this.cls = cls;
            this.conf = conf;
            if (this.conf != null) {
                if (conf.getBoolean("bean.autoCreate")) {
                    this.autoCreate = true;
                }
                if (conf.getBoolean("bean.prototype")) {
                    this.prototype = true;
                }
            }
        }

        public Class getCls() {
            return cls;
        }

        public Object createInst() {
            try {
                return doCreate(cls, new DefaultBeanConfig(conf), null);
            } catch (Exception e) {
                String s = "создание экземпляра для bean [{0}] класс [{1}]";
                String p2 = "";
                if (conf != null) {
                    s += " rt [{2}]";
                    p2 = conf.origin().toString();
                }
                throw new XErrorMark(e, MessageFormat.format(s, getName(), cls.getName(), p2));

            }
        }

        public Object getInst() {
            if (inst == null) {
                if (busy) {
                    throw new XError("Обращение к bean [{0}] в процессе его создания", getName());
                }
                if (prototype) {
                    throw new XError("bean [{0}] является прототипом и нельзя получить его экземпляр", getName());
                }
                synchronized (this) {
                    if (inst == null) {
                        busy = true;
                        try {
                            inst = createInst();
                        } finally {
                            busy = false;
                        }
                    }
                }
            }
            return inst;
        }

        public boolean hasInst() {
            return inst != null;
        }

        public String getName() {
            return name;
        }

        public boolean isPrototype() {
            return prototype;
        }

        public boolean isAutoCreate() {
            return autoCreate;
        }

    }

    ////// factory

    /**
     * Процесс создания экземпляра
     *
     * @param cls    что нужно создать
     * @param cfg    чем конфигурить
     * @param initer чем настраивать
     */
    protected Object doCreate(Class cls, BeanConfig cfg, IBeanIniter initer) throws Exception {

        // создаем экземпляр
        Object inst = cls.newInstance();

        // если созданный экземпляр владеет BeanFactory, то устанавливаем ему себя
        // в качестве родителя перед дальнейшей инициализацией, т.к.
        // фабрика должна быть работоспособной
        if (inst instanceof IBeanFactoryOwner) {
            BeanFactory bh = ((IBeanFactoryOwner) inst).getBeanFactory();
            if (bh != null) {
                BeanFactory bp = bh.getParentBeanFactory();
                if (bp == null) {
                    bh.setParentBeanFactory(this);
                }
            }
        }

        // инициализируем
        beanInit(inst);

        // дополнительная инициализация
        if (initer != null) {
            initer.beanInit(inst);
        }

        // окончательное конфигурирование
        if (inst instanceof IBeanConfigure) {
            ((IBeanConfigure) inst).beanConfigure(cfg);
        } else {
            // dummy config
            if (cfg.hasConf()) {
                if (inst instanceof INamedSet) {
                    String curName = null;
                    if (inst instanceof INamed) {
                        curName = ((INamed) inst).getName();
                    }
                    if (UtString.empty(curName) && !UtString.empty(cfg.getName())) {
                        ((INamedSet) inst).setName(cfg.getName());
                    }
                }
                UtReflect.getUtils().setProps(inst, cfg.getConf());
            }
        }

        return inst;
    }

    public void beanInit(Object inst) {
        // для начала инициализируем через родительскую
        if (getParentBeanFactory() != null) {
            getParentBeanFactory().beanInit(inst);
        }
        // потом через инициализатор по умолчанию
        if (getDefaultBeanIniter() != null) {
            getDefaultBeanIniter().beanInit(inst);
        }
    }

    public Class getClass(String name) {
        // ищем у себя
        BeanDef b = findBean(name);
        if (b != null) {
            return b.getCls();
        }
        // у себя нет, ищем у родителя
        if (getParentBeanFactory() != null) {
            Class cls = getParentBeanFactory().getClass(name);
            if (cls != null) {
                return cls;
            }
        }
        // у родителя нет
        // считаем имя - имя класса и возвращаем его
        Class cls = UtClass.getClass(name);
        cls = UtReflect.getUtils().getDefaultImplementation(cls);
        return cls;
    }

    //////

    public <A> A create(Class<A> cls, BeanConfig cfg, boolean findClassInConf, IBeanIniter initer) {

        try {

            if (cfg == null) {
                cfg = new DefaultBeanConfig();
            }

            // определяем класс
            Class realCls = null;

            if (findClassInConf) {
                String cn = cfg.getConfClassName();
                if (!UtString.empty(cn)) {
                    realCls = getClass(cn);
                }
            }

            if (realCls == null) {
                realCls = cls;
            }

            if (realCls == null) {
                throw new XError("Класс не определен");
            }

            if (realCls.isInterface() || Modifier.isAbstract(realCls.getModifiers())) {
                realCls = getClass(realCls.getName());
            }

            // создаем экземпляр
            Object inst = doCreate(realCls, cfg, initer);

            return (A) inst;

        } catch (Exception e) {
            String s = "создание экземпляра для";
            String p0 = "";
            String p1 = "";
            if (cls != null) {
                s += " класса [{0}]";
                p0 = cls.getName();
            }
            String confOrigin = cfg.getConf().origin().toString();
            if (!UtString.empty(confOrigin)) {
                s += " conf [{1}]";
                p1 = confOrigin;
            }
            throw new XErrorMark(e, MessageFormat.format(s, p0, p1));
        }
    }

    public <A> A create(Class<A> cls, Conf conf, boolean findClassInConf, IBeanIniter initer) {
        return create(cls, new DefaultBeanConfig(conf), findClassInConf, initer);
    }

    public <A> A create(Conf conf, Class<A> cls, boolean findClassInConf, IBeanIniter initer) {
        return create(cls, conf, findClassInConf, initer);
    }

    public <A> A create(BeanConfig cfg, Class<A> cls, boolean findClassInConf, IBeanIniter initer) {
        return create(cls, cfg, findClassInConf, initer);
    }

    public <A> A create(Conf conf, Class<A> cls, IBeanIniter initer) {
        return create(cls, conf, true, initer);
    }

    public <A> A create(BeanConfig cfg, Class<A> cls, IBeanIniter initer) {
        return create(cls, cfg, true, initer);
    }

    public <A> A create(Conf conf, Class<A> cls, boolean findClassInConf) {
        return create(cls, conf, findClassInConf, null);
    }

    public <A> A create(BeanConfig cfg, Class<A> cls, boolean findClassInConf) {
        return create(cls, cfg, findClassInConf, null);
    }

    public <A> A create(Conf conf, Class<A> cls) {
        return create(cls, conf, true, null);
    }

    public <A> A create(BeanConfig cfg, Class<A> cls) {
        return create(cls, cfg, true, null);
    }

    public Object create(Conf conf) {
        return create(null, conf, true, null);
    }

    public Object create(BeanConfig cfg) {
        return create(null, cfg, true, null);
    }

    public Object create(Conf conf, IBeanIniter initer) {
        return create(null, conf, true, initer);
    }

    public Object create(BeanConfig cfg, IBeanIniter initer) {
        return create(null, cfg, true, initer);
    }

    public <A> A create(Class<A> cls) {
        return create(cls, (BeanConfig) null, true, null);
    }

    public <A> A create(Class<A> cls, IBeanIniter initer) {
        return create(cls, (BeanConfig) null, true, initer);
    }

    public Object create(String name, IBeanIniter initer) {
        BeanDef b = findBean(name);
        if (b != null) {
            return create(b.getCls(), (BeanConfig) null, true, initer);
        }
        // пробуем представить имя как класс
        Class cls = UtClass.getClass(name);
        return create(cls, (BeanConfig) null, true, initer);
    }

    public Object create(String name) {
        return create(name, null);
    }

    //////

    public BeanDef getBean(String name) {
        BeanDef b = findBean(name);
        if (b == null) {
            throw new XError("Не найден объект с именем [{0}]", name);
        }
        return b;
    }

    public BeanDef getBean(Class cls) {
        return getBean(cls.getName());
    }

    public BeanDef findBean(Class cls, boolean autoRegister) {
        BeanDef b = findBean(cls.getName());
        if (b == null && autoRegister) {
            b = registerBean(cls.getName(), getClass(cls.getName()));
        }
        return b;
    }

    public BeanDef findBean(String name) {
        return items.get(name);
    }

    //////

    public List<BeanDef> getBeans() {
        List<BeanDef> res = new ArrayList<>();
        res.addAll(itemsOrdered);
        return res;
    }

    protected void registerBean(BeanDef b) {
        synchronized (this) {
            items.put(b.getName(), (BeanDefImpl) b);
            itemsOrdered.add((BeanDefImpl) b);
            itemsDirty = true;
        }
    }

    public BeanDef registerBean(String name, Class cls) {
        BeanDefImpl b = new BeanDefImpl(name, cls, null);
        registerBean(b);
        return b;
    }

    public BeanDef registerBean(String name, Object inst) {
        BeanDefImpl b = new BeanDefImpl(name, inst.getClass(), null);
        b.inst = inst;
        registerBean(b);
        return b;
    }

    public BeanDef registerBean(Conf conf) {
        if (conf == null) {
            return null;
        }
        String cn = conf.getString("class");
        if (UtString.empty(cn)) {
            throw new XError("Для conf [{0}] не указан атрибут class", conf.origin().toString());
        }
        Class cls;
        try {
            cls = getClass(cn);
        } catch (Exception e) {
            throw new XErrorMark(e, MessageFormat.format("получение класса [{0}] для conf [{1}]", cn, conf.origin().toString()));
        }
        BeanDefImpl b = new BeanDefImpl(conf.getName(), cls, conf);
        registerBean(b);
        return b;
    }

    public List<BeanDef> registerBeans(Conf conf) {
        if (conf == null) {
            return null;
        }
        List<BeanDef> res = new ArrayList<>();
        for (Conf x : conf.getConfs()) {
            BeanDef b = registerBean(x);
            res.add(b);
        }
        return res;
    }

    //////

    public BeanFactory getParentBeanFactory() {
        return parentBeanFactory;
    }

    public void setParentBeanFactory(BeanFactory parentBeanFactory) {
        this.parentBeanFactory = parentBeanFactory;
    }

    public IBeanIniter getDefaultBeanIniter() {
        return defaultBeanIniter;
    }

    public void setDefaultBeanIniter(IBeanIniter defaultBeanIniter) {
        this.defaultBeanIniter = defaultBeanIniter;
    }

    //////

    public <A> A bean(Class<A> cls) {
        return (A) getBean(cls.getName()).getInst();
    }

    public Object bean(String name) {
        return getBean(name).getInst();
    }

    public <A> A inst(Class<A> cls) {
        return (A) findBean(cls, true).getInst();
    }

    public <A> List<A> impl(Class<A> clazz) {
        List<A> res = new ArrayList<A>();
        for (BeanDef it : itemsOrdered) {
            if (it.isPrototype()) {
                continue; // прототипы не нужны
            }
            if (clazz.isAssignableFrom(it.getCls())) {
                res.add((A) it.getInst()); // создадутся даже ленивые, и это правильно
            }
        }
        return res;
    }

    //////

    /**
     * Получает на входе конфигурацию, которую получил владелец этого хранилища.
     * Внутри этой conf ищет теги 'bean', по которым и создает свои бины.
     * Этот метод вызывается один раз внутри beanConfigure владельца хранилища.
     */
    public void beanConfigure(BeanConfig cfg) throws Exception {
        Conf conf = cfg.getConf();
        //
        Conf brt = conf.findConf("bean");
        if (brt != null) {
            // имеется описание бинов, регистрируем
            registerBeans(brt);
        }

        // если имеются автосоздаваемые, создаем их
        for (BeanDefImpl b : itemsOrdered) {
            if (b.isAutoCreate()) {
                b.getInst();
            }
        }
    }

}
