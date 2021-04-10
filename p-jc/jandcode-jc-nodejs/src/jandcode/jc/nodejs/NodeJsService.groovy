package jandcode.jc.nodejs

import jandcode.commons.*
import jandcode.commons.error.*
import jandcode.commons.named.*
import jandcode.jc.*
import jandcode.jc.nodejs.impl.*

import java.text.*

/**
 * Поддержка nodejs
 */
class NodeJsService extends CtxService {

    private List<NodeJsModuleProvider> moduleProviders = new ArrayList<NodeJsModuleProvider>()

    protected void onCreate() throws Exception {
        super.onCreate()

        // при создании все загруженные к этому моменту проекты подключаем
        for (Project p : ctx.getProjects()) {
            addProject(p)
        }

        // для новых проектов
        ctx.onEvent(JcConsts.Event_ProjectAfterLoad, { e ->
            Project p = e.project
            addProject(p)
        })

    }

    private void addProject(Project p) {
        if (p == null) {
            return;
        }
        String libPath = UtFile.join(p.wd(JcConsts.LIB_DIR), NodeJsConsts.LIB_NODEJS_DIR);
        if (UtFile.exists(libPath)) {
            addModuleProviderFromPath(libPath);
        }
        NodeJsProject njp = p.getIncluded(NodeJsProject)
        if (njp != null) {
            List<NodeJsModuleProvider> prv = njp.getModuleProviders()
            for (p1 in prv) {
                addModuleProvider(p1)
            }
        }
    }

    private void addModuleProviderFromPath(String libpath) {
        NodeJsModuleProviderPath p = new NodeJsModuleProviderPath(ctx, libpath);
        addModuleProvider(p);
    }

    /**
     * Добавить провайдер nodejs модулей
     */
    void addModuleProvider(NodeJsModuleProvider p) {
        ctx.getLog().debug(MessageFormat.format("add nodejs module provider [{0}]", p));

        // сначала загружаем модули, что бы не загруженные окончательно
        // не попадали в список провайдеров
        p.getModules();

        // чем позже добавлена, тем выше приоритет
        moduleProviders.add(0, p);
    }

    /**
     * Найти модуль nodejs по имени
     * @return null, если не найден
     */
    NodeJsModule findModule(String name) {
        for (NodeJsModuleProvider p : moduleProviders) {
            NodeJsModule z = p.findModule(name);
            if (z != null) {
                return z;
            }
        }
        return null
    }

    /**
     * Найти модуль nodejs по имени
     * @return ошибка, если не найден
     */
    NodeJsModule getModule(String name) {
        NodeJsModule z = findModule(name);
        if (z == null) {
            throw new XError("Module nodejs [{0}] not found", name);
        }
        return z;
    }

    /**
     * Получить список всех библиотек.
     * @return новая копия списка
     */
    NamedList<NodeJsModule> getModules() {
        NamedList<NodeJsModule> tmp = new DefaultNamedList<>();
        for (NodeJsModuleProvider p : moduleProviders) {
            List<NodeJsModule> lst1 = p.getModules();
            for (NodeJsModule z : lst1) {
                if (tmp.find(z.getName()) != null) {
                    continue;
                }
                tmp.add(z);
            }
        }
        return tmp;
    }

    /**
     * Загрузить модули из указанного каталога.
     * @param path каталог
     * @return новый список
     */
    NamedList<NodeJsModule> loadModules(String path) {
        NodeJsModuleProvider pr = new NodeJsModuleProviderPath(ctx, path);
        return pr.getModules();
    }

    /**
     * Добавить файл package.json, каталог с package.json
     * или каталог в формате node_modules.
     */
    void addModulePath(String path) {
        addModuleProviderFromPath(path);
    }

    /**
     * Спиок провайдеров
     */
    List<NodeJsModuleProvider> getModuleProviders() {
        return new ArrayList<>(this.moduleProviders)
    }

}

