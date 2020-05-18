package jandcode.core.jsa.jc

import jandcode.commons.*
import jandcode.commons.error.*
import jandcode.core.jsa.jc.impl.*
import jandcode.jc.*

import java.text.*

/**
 * Поддержка nodejs библиотек
 */
public class NodeJsLibService extends CtxService {

    private List<NodeJsLibProvider> libProviders = new ArrayList<NodeJsLibProvider>();

    protected void onCreate() throws Exception {
        super.onCreate()

        // при создании все загруженные к этому моменту проекты подключаем
        for (Project p : ctx.getProjects()) {
            addProject(p)
        }

    }

    private void addProject(Project p) {
        if (p == null) {
            return;
        }
        String libPath = p.wd(JcConsts.LIB_DIR);
        if (UtFile.exists(libPath)) {
            addLibProviderFromPath(libPath);
        }
    }

    private void addLibProviderFromPath(String libpath) {
        NodeJsLibProviderStd p = new NodeJsLibProviderStd(ctx, libpath);
        addLibProvider(p);
    }

    /**
     * Добавить провайдер nodejs библиотек
     */
    public void addLibProvider(NodeJsLibProvider p) {
        ctx.getLog().debug(MessageFormat.format("add nodejs lib provider [{0}]", p));

        // сначала загружаем библиотеки, что бы не загруженные окончательно
        // не попадали в список провайдеров
        p.getLibs();

        // чем позже добавлена, тем выше приоритет
        libProviders.add(0, p);
    }

    /**
     * Найти библиотеку nodejs по имени
     * @return null, если не найдена
     */
    public NodeJsLib findLib(String name) {
        for (NodeJsLibProvider p : libProviders) {
            NodeJsLib z = p.findLib(name);
            if (z != null) {
                return z;
            }
        }
        return null
    }

    /**
     * Найти библиотеку nodejs по имени
     * @return ошибка, если не найдена
     */
    public NodeJsLib getLib(String name) {
        NodeJsLib z = findLib(name);
        if (z == null) {
            throw new XError("Library nodejs [{0}] not found", name);
        }
        return z;
    }

    /**
     * Получить список всех библиотек.
     * @return новая копия списка
     */
    public NodeJsLibList getLibs() {
        NodeJsLibList tmp = new NodeJsLibList();
        for (NodeJsLibProvider p : libProviders) {
            List<NodeJsLib> lst1 = p.getLibs();
            for (NodeJsLib z : lst1) {
                if (tmp.find(z.getName()) != null) {
                    continue;
                }
                tmp.add(z);
            }
        }
        return tmp;
    }

    /**
     * Загрузить библиотеки из указанного каталога.
     * @param path каталог
     * @return новый список
     */
    public NodeJsLibList loadLibs(String path) {
        NodeJsLibProviderStd pr = new NodeJsLibProviderStd(ctx, path);
        return pr.getLibs();
    }

    /**
     * Добавить каталог с библиотекам
     */
    public void addLibDir(String path) {
        addLibProviderFromPath(path);
    }

    /**
     * Спиок провайдеров
     */
    public List<NodeJsLibProvider> getLibsProviders() {
        return new ArrayList<>(this.libProviders)
    }

}

