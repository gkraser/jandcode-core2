package jandcode.commons.moduledef;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.moduledef.impl.*;
import org.apache.commons.vfs2.*;

import java.util.*;

/**
 * Статические утилиты для ModuleDef
 */
public class UtModuleDef {

    /**
     * Создать экземпляр описания модуля.
     *
     * @param name        имя модуля
     * @param rootPath    корневой каталог модуля
     * @param packageRoot имя пакета модуля. Если null, то пакет равен имени модуля
     * @param moduleFile  файл с конфигурацией модуля. Может быть null, тогда берется автоматически
     * @return определение модуля
     */
    public static ModuleDef createModuleDef(String name, String rootPath, String packageRoot, String moduleFile) {
        return new ModuleDefImpl(name, rootPath, packageRoot, moduleFile);
    }

    /**
     * Создать стандартный ресолвер модулей
     */
    public static ModuleDefResolver createModuleDefResolver() {
        return new ModuleDefResolverImpl();
    }

    /**
     * Загрузка конфигурации {@link ModuleDef}
     *
     * @param moduleDef         для какого {@link ModuleDef} загрузить конфигурацию
     * @param moduleDefResolver какой {@link ModuleDefResolver} использовать, если нужно
     * @param vars              переменные, которые будут доступны при загрузке
     * @return загруженный экземпляр конфигурации
     * @throws Exception
     */
    public static ModuleDefConfig loadModuleDefConfig(ModuleDef moduleDef,
            ModuleDefResolver moduleDefResolver, Map<String, String> vars) throws Exception {

        if (moduleDefResolver == null) {
            moduleDefResolver = createModuleDefResolver();
        }

        ModuleDefConfig res = new ModuleDefConfigImpl();

        ConfLoader ldr = UtConf.createLoader(res.getConf());
        ldr.registerPlugin(new ModuleDefConfLoaderPlugin(moduleDef, res, moduleDefResolver, vars));

        // загружаем дескриптор модуля первым, если он присутсвует
        FileObject moduleInfoFile;
        try {
            moduleInfoFile = UtFile.getFileObject("res:META-INF/jc-module/" + moduleDef.getName() + "/module-info.cfx");
        } catch (Exception e) {
            moduleInfoFile = null;
        }
        if (moduleInfoFile != null) {
            ldr.load().fromFileObject(moduleInfoFile);
        }

        ldr.load().fromFileObject(moduleDef.getModuleFile());

        res.getFiles().addAll(ldr.getLoadedFiles());

        return res;
    }

}
