package jandcode.jc.std

import jandcode.commons.*
import jandcode.commons.vdir.*
import jandcode.jc.*

/**
 * Сервис содержит виртуальный каталог с каталогами jc-data из всех загруженных
 * проектов и распознанных jar
 */
class JcDataService extends CtxService implements IVDirWrap {

    private VDirWrap vdir = new VDirWrap(new VDirLocal());

    protected void onCreate() throws Exception {
        super.onCreate()

        // при создании все загруженные к этому моменту проекты подключаем
        for (Project p : ctx.getProjects()) {
            addProject(p)
        }

        // при создании все загруженные провайдеры библиотек подключаем
        for (ILibProvider p : ctx.getLibsProviders()) {
            addLibProvider(p)
        }

        // для новых проектов  
        ctx.onEvent(JcConsts.Event_ProjectAdded, { e ->
            Project project = e.project
            addProject(project)
        });

        // для новых провайдеров библиотек
        ctx.onEvent(JcConsts.Event_LibProviderAdded, { e ->
            ILibProvider provider = e.provider
            addLibProvider(provider)
        });

    }

    private void addProject(Project p) {
        if (p == null) {
            return;
        }
        String projectJcData = p.wd(JcConsts.JC_DATA_DIR);
        if (UtFile.exists(projectJcData)) {
            getVdir().addRoot(projectJcData);
        }
    }

    private void addLibProvider(ILibProvider p) {
        if (p == null) {
            return;
        }

        // библиотеки могут содержать ресурсы,добавляем их
        // добавляемые библиотеки могут иметь jc-данные
        for (Lib lib : p.getLibs()) {
            if (lib.isSys() || lib.getSourceProject() != null) {
                continue; // пропускаем системные и в исходниках
            }
            String jarFile = lib.getJar();
            if (!UtString.empty(jarFile) && UtFile.exists(jarFile)) {
                JarCacheService jsvc = ctx.service(JarCacheService);
                if (jsvc.hasJcData(jarFile)) {
                    String dir = jsvc.getJcDataDir(jarFile);
                    getVdir().addRoot(dir);
                }
            }
        }
    }

    //////

    VDir getVdir() {
        return vdir.getVdir();
    }

    String findFile(String path) {
        return vdir.findFile(path);
    }

    String getFile(String path) {
        return vdir.getFile(path);
    }

    List<String> findFiles(String path) {
        return vdir.findFiles(path);
    }

}
