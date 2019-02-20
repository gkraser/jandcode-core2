package jandcode.jc.impl

import jandcode.commons.*
import jandcode.commons.moduledef.*
import jandcode.commons.moduledef.impl.*
import jandcode.commons.simxml.*
import jandcode.jc.*
import jandcode.jc.std.*

/**
 * Всякие утилиты для moduledef
 */
class ModuleDefProjectUtils {

    ProjectScript ps

    ModuleDefProjectUtils(ProjectScript ps) {
        this.ps = ps
    }

    /**
     * Генерация файла с описанием модулей
     */
    void generateRegistryModuleDef() {
        String fn = ps.wd(ModuleDefConsts.FILE_REGISTRY_MODULE_DEF)
        ps.log.info("Generate registry modules: ${fn}")

        //
        SimXml x = new SimXmlNode()
        x.setValue("#comment:#text", "THIS FILE GENERATED, NOT MODIFY!")

        SimXml x1 = x.addChild("system")

        //
        ListLib libs = ps.ctx.getLibs()
        for (Lib lib in libs) {

            Project p = lib.sourceProject
            if (p == null) {
                continue
            }

            JavaProject jp = p.getIncluded(JavaProject)
            if (jp == null) {
                continue
            }

            for (ModuleDef md in jp.getModuleDefs()) {
                SimXml x3 = x1.addChild("module-def")
                ModuleDefUtilsImpl.saveModuleDefToXml(md, x3)
            }
        }

        // записываем результат
        SimXmlSaver svr = new SimXmlSaver(x)
        UtFile.cleanFile(fn)
        svr.save().toFile(fn)

        // записываем хеш
        String hash = calcModulesHash(libs)
        UtFile.saveString(hash, new File(fn + '.hash'))
    }

    protected String calcModulesHash(ListLib libs) {
        String hash = ''
        for (Lib lib in libs) {
            if (lib.sourceProject == null) continue
            if (lib.sourceProject.getIncluded(JavaProject) == null) continue
            //
            hash += lib.sourceProject.projectFile +
                    '|' + new File(lib.sourceProject.projectFile).lastModified() +
                    '|' + lib.sourceProject.wd() +
                    '|' + new File(lib.sourceProject.wd()).lastModified() +
                    '|'
        }
        hash = UtString.md5Str(hash)
        return hash
    }

    protected String loadModulesHash() {
        String fn = ps.wd(ModuleDefConsts.FILE_REGISTRY_MODULE_DEF) + '.hash'
        if (!UtFile.exists(fn)) {
            return ""
        }
        return UtFile.loadString(fn)
    }

    /**
     * Обновление файла с описанием модулей, если необходимо
     */
    void updateRegistryModuleDef() {
        String mdf = ps.wd(ModuleDefConsts.FILE_REGISTRY_MODULE_DEF)
        File mdfFile = new File(mdf)
        boolean hasSource = hasSource()
        if (!hasSource) {
            if (mdfFile.exists()) {
                mdfFile.delete()
            }
        } else {
            if (mdfFile.exists()) {
                ListLib libsAll = ps.ctx.getLibs()

                String hash1 = loadModulesHash()
                String hash2 = calcModulesHash(libsAll)
                if (hash1 != hash2) {
                    generateRegistryModuleDef()
                }

            } else {
                // файла нет, а должен быть
                generateRegistryModuleDef()
            }
        }
    }

    /**
     * true, если среди библиотек есть библиотеки в исходниках
     */
    boolean hasSource() {
        def rlibs = ps.ctx.getLibs()
        boolean hasSource = false
        for (lib in rlibs) {
            if (lib.sourceProject != null) {
                hasSource = true
                break
            }
        }
        return hasSource
    }

}
