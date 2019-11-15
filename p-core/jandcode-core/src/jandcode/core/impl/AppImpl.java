package jandcode.core.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.env.*;
import jandcode.commons.error.*;
import jandcode.commons.event.*;
import jandcode.commons.moduledef.*;
import jandcode.core.*;
import org.apache.commons.vfs2.*;
import org.slf4j.*;

import java.util.*;

public class AppImpl implements App, IBeanIniter {

    protected static Logger log = LoggerFactory.getLogger(App.class);

    static {
        UtError.addErrorConvertor(new ErrorConvertorApp());
    }

    private ModuleHolder moduleHolder;
    private String appConfFile;
    private Conf conf;
    private String appdir;
    private String workdir;
    private Env env;
    private boolean test;
    private List<ConfSource> confSources;
    protected Map<String, Object> props = new LinkedHashMap<>();

    private BeanFactory beanFactory;
    private String appName;

    private int startupLevel;

    public void beanInit(Object inst) {
        if (inst instanceof IAppLinkSet) {
            ((IAppLinkSet) inst).setApp(this);
        }
    }

    /**
     * исходник conf для отслеживания изменившихся файлов
     */
    class ConfSource {

        FileObject file;
        long dmod;

        public ConfSource(String filename) {
            this.file = UtFile.getFileObject(filename);
            try {
                dmod = this.file.getContent().getLastModifiedTime();
            } catch (FileSystemException e) {
            }
        }

        public boolean isModify() {
            long dmod1 = 0;
            try {
                dmod1 = this.file.getContent().getLastModifiedTime();
            } catch (FileSystemException e) {
            }
            return dmod1 > dmod;
        }
    }

    class AppConfHandlerItem {

        Conf conf;
        Module moduleOwner;
        AppConfHandler handler;

        public AppConfHandlerItem(Conf conf, Module moduleOwner) {
            this.conf = conf;
            this.moduleOwner = moduleOwner;
            try {
                this.handler = (AppConfHandler) UtClass.createInst(conf.getString("class"));
            } catch (Exception e) {
                throw new XErrorMark(e, "создание app-conf-handler из узла: " + conf.origin());
            }
        }

        public void handleAppConf(Module module) throws Exception {
            if (module == null) {
                // для приложения
                handler.handleAppConf(AppImpl.this, this.moduleOwner, this.conf);
            } else {
                // для модуля
                handler.handleModuleConf(module, this.moduleOwner, this.conf);
            }
        }
    }

    //////

    public AppImpl(String appConfFile, boolean test) throws Exception {
        FileObject f = UtFile.getFileObject(appConfFile);
        if (!f.exists()) {
            throw new XError("Файл не найден: " + f.toString());
        }
        this.test = test;
        this.appConfFile = f.toString();
        this.env = new DefaultEnv(true, this.test);
        //                       
        loadAppConf();
    }

    public ModuleHolder getModules() {
        return moduleHolder;
    }

    public Conf getConf() {
        return conf;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public String getAppdir() {
        return appdir;
    }

    public String getWorkdir() {
        return workdir;
    }

    public String getAppConfFile() {
        return appConfFile;
    }

    public boolean isDebug() {
        return getEnv().isDev();
    }

    public boolean isTest() {
        return test;
    }

    public Env getEnv() {
        return env;
    }

    public Map<String, Object> getProps() {
        return props;
    }

    //////

    private void loadAppConf() throws Exception {

        // для начала каталогом приложения считаем каталог с конфигурацией приложения
        String appConfPath = UtFile.path(UtFile.vfsPathToLocalPath(appConfFile));
        this.appdir = appConfPath;

        // resolver
        ModuleDefResolver moduleDefResolver = UtModuleDef.createModuleDefResolver();
        moduleDefResolver.addWorkDir(appConfPath);

        ModuleHolderImpl tmpMh = new ModuleHolderImpl(this, moduleDefResolver);
        EventHandler<ModuleHolderImpl.Event_ModuleConfLoaded> handlerCfgLoaded = (e) -> {
            if (AppConsts.MODULE_APP.equals(e.getModuleDef().getName())) {
                // загрузка модуля app, остальные модули еще не грузились
                Conf conf = e.getModuleDefConfig().getConf();

                // env
                String envValue = conf.getString("app/env");
                if ("dev".equals(envValue)) {
                    this.env = new DefaultEnv(false, this.test);
                }

                // если <app appdir="DIR"/> определено, устанавливаем appdir
                String s = conf.getString("app/appdir");
                if (!UtString.empty(s)) {
                    this.appdir = UtFile.abs(UtFile.vfsPathToLocalPath(s));
                }

            }
        };
        tmpMh.getEventBus().onEvent(ModuleHolderImpl.Event_ModuleConfLoaded.class, handlerCfgLoaded);

        // ядро
        tmpMh.addModule(moduleDefResolver.getModuleDef("jandcode.core"));

        // app
        Module appModule = tmpMh.addModule(UtModuleDef.createModuleDef(AppConsts.MODULE_APP, appConfPath, "", appConfFile));

        // убиарем подписчика
        tmpMh.getEventBus().unEvent(ModuleHolderImpl.Event_ModuleConfLoaded.class, handlerCfgLoaded);

        // загружено все, включая все зависимости

        // загрузка закончена
        moduleHolder = tmpMh;
        conf = UtConf.create();   // временная!

        //
        workdir = UtFile.getWorkdir();

        // создаем обработчики conf
        List<AppConfHandlerItem> appConfHandlerItems = new ArrayList<>();
        for (Module m : moduleHolder) {
            for (Conf x : m.getConf().getConfs(AppConsts.APP_CONF_HANDLER)) {
                AppConfHandlerItem it = new AppConfHandlerItem(x, m);
                appConfHandlerItems.add(it);
            }
        }

        if (appConfHandlerItems.size() > 0) {
            // выполняем обработчики для модулей
            for (Module m : moduleHolder) {
                for (AppConfHandlerItem it : appConfHandlerItems) {
                    it.handleAppConf(m);
                }
            }
        }

        //

        // объединяем для App.getConf()
        Conf tmpRt = UtConf.create();
        for (Module m : tmpMh) {
            tmpRt.join(m.getConf());
        }
        conf = tmpRt; // окончательная

        if (appConfHandlerItems.size() > 0) {
            // выполняем обработчики для приложения
            for (AppConfHandlerItem it : appConfHandlerItems) {
                it.handleAppConf(null);
            }
        }

        // собираем файлы для анализа их изменений
        confSources = new ArrayList<ConfSource>();
        for (String rtf : tmpMh.getFiles()) {
            ConfSource rs = new ConfSource(rtf);
            confSources.add(rs);
        }

        // appname
        String s = UtVDir.normalize(getConf().getString("app/appname"));
        if (!UtString.empty(s)) {
            this.appName = s;
        } else {
            this.appName = UtFile.filename(this.appdir);
        }

        // создаем bean
        beanFactory = new DefaultBeanFactory(this);
        BeanConfig cfg = new DefaultBeanConfig(getConf());
        beanFactory.beanConfigure(cfg);

        // уведомлям о загрузке
        for (IAppLoaded z : impl(IAppLoaded.class)) {
            z.appLoaded();
        }

        // готово
    }

    /**
     * Ищет изменившийся файл conf из тех, которые были загружены в приложение.
     * Если найден, возвращается полное имя файла.
     * Иначе - null. Работает только в отладочном режиме.
     */
    public String findModifyRtSource() {
        if (!isDebug() || confSources == null) {
            return null;
        }
        for (ConfSource rs : confSources) {
            if (rs.isModify()) {
                return rs.file.toString();
            }
        }
        return null;
    }

    public String getAppName() {
        return appName;
    }

    //////

    public void startup() {
        this.startupLevel++;
        if (this.startupLevel == 1) {
            try {
                for (IAppStartup z : impl(IAppStartup.class)) {
                    z.appStartup();
                }
            } catch (Exception e) {
                throw new XErrorWrap(e);
            }
        }
    }

    public void shutdown() {
        if (this.startupLevel == 0) {
            return;
        }
        this.startupLevel--;
        if (this.startupLevel == 0) {
            try {
                for (IAppShutdown z : impl(IAppShutdown.class)) {
                    z.appShutdown();
                }
            } catch (Exception e) {
                throw new XErrorWrap(e);
            }
        }
    }

}
