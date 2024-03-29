package jandcode.jc.std

import jandcode.commons.*
import jandcode.commons.variant.*
import jandcode.jc.*

/**
 * Шаблон проекта.
 * Используется в проектах шаблонах.
 * Подключается в файл '-projecttml.jc' и настраивается.
 */
class Projecttml extends ProjectScript {

    /**
     * Имя файла projecttml по умолчанию
     */
    public static String PROJECTTML_FILE = "-projecttml.jc"

    ////// настройки

    /**
     * Краткое описание проекта
     */
    String desc = "нет описания"

    /**
     * Список дополнительных опций для этого шаблона
     */
    List opts = []

    /**
     * Список замен: ключ - что заменять, значение - на что заменять.
     */
    Map replaces = [:]

    /**
     * Выполняется перед началом генерации.
     * При выполнении известны outdir и args.
     */
    Closure init

    /**
     * Выполняется после генерации в каталоге назначения
     */
    Closure done

    /**
     * Если closure установлена, то именно она будет использоваться для генерации
     * проекта. Т.е. шаблон занимается вообще всем сам.
     */
    Closure gen

    /**
     * Расширения текстовых файлов, в которых будет производится замена текста
     */
    List textfiles = [
            "java", "txt", "xml", "properties", "groovy", "xconf",
            "gsp", "html", "htm", "css", "jsp", "jc", "xc", "js", "cfx",
            "vue", "json",
    ]

    /**
     * Исключаемые из шаблона файлы
     */
    List excludes = [
            "**/temp", "**/temp/**/*", "**/out", "**/out/**/*",
            "**/_product", "**/_product/**/*",
            "**/_jc", "**/_jc/**/*",
            "**/_gen", "**/_gen/**/*",
            "**/*.iws", "**/*.iml", "**/*.ipr", "**/*.log",
            "**/*.tmp", "**/Thumbs.db",
            "**/node_modules", "**/node_modules/**/*",
    ]

    ////// утилиты

    /**
     * В какой каталог собираемся генерировать проект.
     * Известен только при начале генерации.
     */
    String outdir

    /**
     * Аргументы командной строки для команды create.
     * Известен только при начале генерации.
     */
    IVariantMap args

    protected void onInclude() throws Exception {
        include(CleanProject)
        onEvent(CleanProject.Event_Clean, this.&cleanHandler)
    }

    void cleanHandler() {
        ant.delete(includeemptydirs: true) {
            fileset(dir: wd) {
                for (ex in excludes) {
                    include(name: ex)
                }
            }
        }
    }

    /**
     * Опции по умолчанию для java-модуля.
     */
    public List getOptsJavaProject() {
        return [
                cm.opt("n", "", "Имя проекта. По умолчанию имя выходного каталога"),
                cm.opt("p", "", "Имя java-пакета. По умолчанию формируется по имени проекта"),
        ]
    }

    /**
     * Инициализация по умолчанию для java-модуля
     * @param nameTemplate шаблон имени для содержимого шаблона. Должен быть в формате
     *        'name1-name2'. Если не задано, используется имя проекта шаблона.
     */
    public void initJavaProject(String nameTemplate = "") {
        if (UtString.empty(nameTemplate)) {
            nameTemplate = project.name
        }
        String[] ar = nameTemplate.split("\\-")
        if (ar.length < 2) {
            error("Имя проекта должно быть в формате набора имен с разделителем '-'")
        }
        // замены
        def pname = args.getString("n", UtFile.filename(outdir))
        def Pname = UtString.camelCase(pname)
        def pak = args.getString("p", UtString.unCamelCase(Pname, (char) '.'))
        //

        def keyMaker = { String delim, boolean capFirst = false ->
            String s = ""
            for (part in ar) {
                if (s.length() > 0) {
                    s += delim
                }
                if (capFirst) {
                    s += UtString.capFirst(part)
                } else {
                    s += part
                }
            }
            return s
        }

        replaces[keyMaker("-")] = pname
        replaces[keyMaker("", true)] = Pname
        replaces[keyMaker(".")] = pak
        replaces[keyMaker("/")] = pak.replace(".", "/")
        replaces[keyMaker("_")] = pak.replace(".", "_")

    }

}
