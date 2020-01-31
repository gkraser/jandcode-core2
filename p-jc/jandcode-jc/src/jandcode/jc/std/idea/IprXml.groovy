package jandcode.jc.std.idea

import jandcode.commons.*
import jandcode.commons.simxml.*
import jandcode.jc.*
import jandcode.jc.std.*

/**
 * Класс для ipr представления xml
 */
class IprXml extends BaseXml {

    IprXml(Project project, SimXml root) {
        super(project, root)
    }

    /**
     * Информация о модулях
     */
    SimXml getRootModules() {
        return root.findChild("component@name=ProjectModuleManager/modules", true)
    }

    /**
     * Удалить все модули
     */
    void removeModules() {
        rootModules.clearChilds()
    }

    /**
     * Добавить проект как модуль
     * @return добавленный xml узел
     */
    SimXml addModule(Project m) {
        String pn = getIdeaProjectName(m)
        SimXml xm = rootModules
        def x1 = xm.addChild("module")
        x1["fileurl"] = "file://${m.wd}/${pn}.iml"
        x1["filepath"] = "${m.wd}/${pn}.iml"
        return x1
    }

    /**
     * Добавить модули из списка библиотек.
     * Добавляются только библиотеки с исходниками.
     */
    void addModules(ListLib libs) {
        for (Lib lib in libs) {
            if (lib.sourceProject != null) {
                addModule(lib.sourceProject)
            }
        }
    }

    /**
     * Добавить конфигурацию запуска
     * @param name имя конфигурации
     * @param templatePath vfs путь до шаблона конфигурации.
     * Корнем должен быть узел "configuration"
     * @return добавленная конфигурация, можно настраивать далее
     */
    SimXml addRunConfig(String name, String templatePath) {
        def x1 = root.findChild("component@name=ProjectRunConfigurationManager/configuration@name=${name}")
        if (x1 == null) {
            x1 = new SimXmlNode()
            ctx.debug("idea template: " + templatePath)
            x1.load().fromFileObject(templatePath)
            x1['name'] = name
            def x2 = x1.findChild("module@name={MODULE}")
            if (x2 != null) {
                x2['name'] = getIdeaProjectName()
            }
            def xr = root.findChild("component@name=ProjectRunConfigurationManager", true)
            xr.addChild(x1)
        }
        return x1
    }

    /**
     * Добавить конфигурацию запуска утилиты jc
     * @param name имя конфигурации
     * @param cliArguments аргументы командной строки запуска jc
     * @param workdir рабочиий каталог для запуска
     * @param vmParameters аргументы для виртуальной машины
     * @return добавленная конфигурация, можно настраивать далее
     */
    SimXml addRunConfig_jc(String name, String cliArguments, String workdir = "", String vmParameters = "") {
        SimXml z = addRunConfig(name, ctx.service(JcDataService).getFile("idea/run-main-template.xml"))
        z['option@name=MAIN_CLASS_NAME:value'] = Main.class.getName()
        String vma = ""
        String appdir = System.getProperty(JcConsts.PROP_APP_DIR);
        if (!UtString.empty(appdir)) {
            vma += " -D" + JcConsts.PROP_APP_DIR + "=" + appdir
        }
        if (!UtString.empty(vmParameters)) {
            vma += vmParameters
        }
        z['option@name=VM_PARAMETERS:value'] = vma.trim()
        z['option@name=PROGRAM_PARAMETERS:value'] = cliArguments.trim()

        if (!UtString.empty(workdir)) {
            z['option@name=WORKING_DIRECTORY:value'] = UtFile.getFileObject(workdir).toString()
        }

        // envs
        SimXml envs = z.findChild('envs', true)
        if (ctx.getConfig().getAutoLoadProjects().size() > 0) {
            SimXml e1 = envs.addChild('env')
            e1['name'] = JcConsts.ENV_JC_PATH
            e1['value'] = UtString.join(ctx.getConfig().getAutoLoadProjects(), UtFile.isWindows() ? ";" : ":")
        }

        return z
    }

    /**
     * Добавить конфигурацию запуска mainClass
     * @param name имя конфигурации
     * @param cliArguments аргументы командной строки запуска jc
     * @param workdir рабочиий каталог для запуска
     * @param vmParameters аргументы для виртуальной машины
     * @return добавленная конфигурация, можно настраивать далее
     */
    SimXml addRunConfig_main(String name, String mainClass, String cliArguments,
            String workdir, List<String> vmParameters, Map<String, Object> envVars) {
        SimXml z = addRunConfig(name, ctx.service(JcDataService).getFile("idea/run-main-template.xml"))

        // mainClass
        z['option@name=MAIN_CLASS_NAME:value'] = mainClass


        // vm parameters
        String vma = ""
        if (vmParameters != null) {
            for (vmp in vmParameters) {
                vma += " " + vmp
            }
        }
        z['option@name=VM_PARAMETERS:value'] = vma.trim()

        // cli
        if (cliArguments == null) {
            cliArguments = ""
        }
        z['option@name=PROGRAM_PARAMETERS:value'] = cliArguments.trim()

        // workdir
        if (!UtString.empty(workdir)) {
            z['option@name=WORKING_DIRECTORY:value'] = UtFile.vfsPathToLocalPath(UtFile.getFileObject(workdir).toString())
        }

        // envs
        SimXml envs = z.findChild('envs', true)
        if (envVars != null) {
            for (ev in envVars) {
                SimXml e1 = envs.addChild('env')
                e1['name'] = ev.key
                e1['value'] = UtCnv.toString(ev.value)

            }
        }

        return z
    }

}
