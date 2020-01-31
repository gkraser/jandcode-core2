package jandcode.jc.test

import groovy.ant.*
import jandcode.commons.*
import jandcode.commons.error.*
import jandcode.commons.test.*
import jandcode.jc.*
import jandcode.jc.impl.*

//todo app исключено!!!

/**
 * Расширение для тестов jc. Поддержка проектов jc в каталоге модуля test.projects.
 *
 * Для корректной работы необходимо обеспечить нахождение каталога test.projects,
 * т.к. тесты могут запускаться откуда угодно, в том числе и из другого проекта.
 *
 * Для настройки используется AppTestSvc.
 * В файле test.cfx дополнительно нужно написать:
 *
 * <pre>{@code
 * <unittest>
 *   <test.projects
 *      module = "my.module.name"
 *   />
 * </unittest>
 *}</pre>
 *
 * где `my.module.name` имя модуля, который тестируется. Имеенно в корне проекта
 * этого модуля и будет искаться каталог test.projects
 *
 * Как пользоватся.
 *
 * Сначала берем среду: {@code env = getEnv(NAME)}. Это копия каталога test.projects с указанным именем.
 * Для каждого имени своя копия. Потом через среду загружаем проект: {@code p = env.load(PROJECT_PATH)},
 * где PROJECT_PATH путь относительно каталога с копией test.projects.
 *
 * Далее с проектом можно работать, например через метод script или напрямую.
 *
 */
class JcTestProjectsTestSvc extends BaseTestSvc {

    static Map<String, ProjectsEnv> projectsEnvs = new HashMap<>()

    //AppTestSvc app

    class ProjectsEnv {

        Ctx _ctx
        String name
        String key

        /**
         * Каталог со средой
         */
        String path

        ProjectsEnv(String name, String key) {
            this.name = name
            this.key = key
        }

        /**
         * Загрузить указанный проект
         * @param path путь относительно каталога test.projects
         */
        Project load(String path) {
            return getCtx().load(this.path + "/" + path)
        }

        Ctx getCtx() {
            if (_ctx == null) {
                String vn = "unittest/test.projects:module"
                String moduleName = app.conf.getString(vn)
                if (UtString.empty(moduleName)) {
                    throw new XError("Не указана переменная ${vn} в файле ${app.appConfFile}")
                }
                def module = app.getModules().find(moduleName)
                if (module == null) {
                    throw new XError("Не найден модуль ${moduleName} указанный в ${vn}")
                }
                String modulePath = module.getSourceInfo().getProjectPath()
                if (UtString.empty(modulePath)) {
                    throw new XError("Модуль ${moduleName} из переменной ${vn} не имеет исходников")
                }

                String testProjectsPath = UtFile.join(modulePath, "test.projects")
                if (!UtFile.exists(testProjectsPath)) {
                    throw new XError("Не существует каталог ${testProjectsPath} в модуле ${module} указанный в ${vn} в файле ${app.appConfFile}")
                }

                // создаем рабочий каталог
                this.path = UtFile.abs("temp/test.projects/${moduleName}--${this.name}--${this.key}")
                AntBuilder ant = new AntBuilder();
                ant.getProject().setBasedir(testProjectsPath);

                // чистим
                ant.mkdir(dir: this.path);
                ant.delete(includeemptydirs: true) {
                    fileset(dir: this.path, defaultexcludes: false) {
                        include(name: "**/*")
                    }
                }

                // копируем
                ant.copy(todir: this.path, preservelastmodified: true) {
                    fileset(dir: testProjectsPath, defaultexcludes: false) {
                        include(name: "**/*")
                        exclude(name: "**/temp")
                        exclude(name: "**/temp/**/*")
                        exclude(name: "**/_jc")
                        exclude(name: "**/_jc/**/*")
                        exclude(name: "**/out")
                        exclude(name: "**/out/**/*")
                        exclude(name: "**/*.tmp")
                        exclude(name: "**/*.log")
                    }
                }

                // создаем контекст
                _ctx = doLoadCtx()
            }
            return _ctx
        }

        void reloadCtx() {
            if (_ctx == null) {
                getCtx()
            } else {
                _ctx = doLoadCtx()
            }
        }

        private Ctx doLoadCtx() {
            // создаем контекст

            JcConfig cfg = JcConfigFactory.load(this.path);

            Ctx tmp = CtxFactory.createCtx()
            tmp.applyConfig(cfg);

            // готово
            return tmp
        }

    }

    public static class DummyProjectScript extends ProjectScript {
        protected void onInclude() throws Exception {
        }
    }

    void setUp() throws Exception {
        super.setUp()

        // связанное тестовое приложение. Тут берем конфигурацию
        //app = testSvc(AppTestSvc)
    }

    //////

    /**
     * Получить среду с указанным именем.
     * Среда представляет собой копию test.projects в каталоге temp привязанной к имени.
     * Каждая среда создается только один раз за сеанс.
     * @param name
     * @return
     */
    ProjectsEnv getEnv(String name) {
        String key = UtString.md5Str(app.getAppConfFile() + "#" + name)
        ProjectsEnv env = projectsEnvs.get(key)
        if (env == null) {
            env = new ProjectsEnv(name, key)
            projectsEnvs.put(key, env)
        }
        return env
    }

    /**
     * help для команды
     */
    void help(Project p, String cmName) {
        MainImpl main = new MainImpl()
        println(main.helpCm(p, p.cm.get(cmName)))
    }

    /**
     * help для проекта
     */
    void help(Project p) {
        MainImpl main = new MainImpl()
        println(main.help(p))
    }

    /**
     * Создает dummy скрипт для проекта для использования: <code>script(p).with {}</code>
     */
    public ProjectScript script(Project p) {
        DummyProjectScript sr = new DummyProjectScript()
        sr.setProject(p)
        return sr
    }

    /**
     * Выполнить команду для проекта
     * @param p проект
     * @param cmName команда
     * @param args аргументы
     */
    public void execCm(Project p, String cmName, Map args = null) {
        Cm cm = p.cm.get(cmName)
        cm.exec(args)
    }

}
