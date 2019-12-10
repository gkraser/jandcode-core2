package jandcode.core.web.std.mount;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.moduledef.*;
import jandcode.core.*;
import jandcode.core.web.*;
import jandcode.core.web.virtfile.*;
import jandcode.core.web.virtfile.impl.virtfs.*;
import org.apache.commons.vfs2.FileType;
import org.apache.commons.vfs2.*;
import org.slf4j.*;

import java.util.*;

/**
 * Поставщик mount для модулей с настройкой module-mount.
 * Так же монтирует сгенерированные каталоги с исходниками.
 */
public class ModuleMount extends BaseMount implements IMountProvider {

    protected static Logger log = LoggerFactory.getLogger(ModuleMount.class);

    public static final String CONF_SPECIAL_FOLDER = "special-folder";
    public static final String CONF_MOUNT = "mount";

    private Map<String, String> specialFolders = new LinkedHashMap<>();

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        Conf conf = cfg.getConf();
        //
        // настройка спецпапок модуля
        for (Conf x : conf.getConfs(CONF_SPECIAL_FOLDER)) {
            specialFolders.put(UtConf.getNameAsPath(x), x.getString("virtualPath"));
        }
    }

    public List<Mount> loadMounts() throws Exception {
        List<Mount> res = new ArrayList<>();

        for (Module module : UtWeb.getMountModules(getApp())) {
            Conf mmrt = module.getConf().findConf(UtWeb.CONF_MOUNT_MODULE);

            if (mmrt.getBoolean("default", true)) {
                // есть ли сгенерированные исходники?
                ModuleDefSourceInfo sm = module.getSourceInfo();
                boolean genSrcExists = sm.isSource() && sm.getSrcGenPaths().size() > 0;

                // счетчик сгенеренных
                int numgen = 0;

                if (genSrcExists && !getApp().getEnv().isTest()) {
                    // если исходники не сгенерированы,
                    // просто создаем каталоги. Но не в тестах.
                    FileObject f = UtFile.getFileObject(sm.getSrcGenPaths().get(0));
                    if (!f.exists() || f.getChildren().length == 0) {
                        for (String gen : sm.getSrcGenPaths()) {
                            FileObject f1 = UtFile.getFileObject(gen + "/" + module.getVPath());
                            f1.createFolder();
                        }
                    }
                }


                // монтируем модуль
                String nm = module.getName() + "--mount-module";
                res.add(createMountVfs(
                        nm,
                        module.getVPath(),
                        module.getPath()
                ));
                if (genSrcExists) {
                    // монтируем сгенеренные так же, как и модуль
                    for (String gen : sm.getSrcGenPaths()) {
                        FileObject f = UtFile.getFileObject(gen + "/" + module.getVPath());
                        if (needMount(f)) {
                            numgen++;
                            res.add(createMountVfs(
                                    nm + "--gensrc--" + numgen + "--" + UtFile.filename(gen),
                                    module.getVPath(),
                                    f.toString()
                            ));
                        }
                    }
                }

                // спец папки
                for (String key : specialFolders.keySet()) {
                    String val = specialFolders.get(key);
                    FileObject fspec = UtFile.getFileObject(module.getPath() + "/" + key);
                    nm = module.getName() + "--mount-module--" + key.replace('/', '.');
                    if (fspec.exists()) {
                        res.add(createMountVfs(
                                nm,
                                val,
                                fspec.toString()
                        ));
                    }
                    if (genSrcExists) {
                        // и сгенеренные тоже
                        for (String gen : sm.getSrcGenPaths()) {
                            FileObject f = UtFile.getFileObject(gen + "/" + module.getVPath() + "/" + key);
                            if (needMount(f)) {
                                numgen++;
                                res.add(createMountVfs(
                                        nm + "--gensrc--" + numgen + "--" + UtFile.filename(gen),
                                        val,
                                        f.toString()
                                ));
                            }
                        }
                    }
                }

            }

            // дочерние mount
            for (Conf chmm : mmrt.getConfs(CONF_MOUNT)) {
                res.add(getApp().create(chmm, MountVfs.class));
            }

        }

        return res;
    }

    /**
     * Нужно ли монтировать сгенеренный каталог
     */
    private boolean needMount(FileObject f) throws Exception {
        return f.exists() && f.getType() == FileType.FOLDER;
    }

}
