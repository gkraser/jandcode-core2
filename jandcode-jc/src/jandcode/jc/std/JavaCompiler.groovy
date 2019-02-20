package jandcode.jc.std

import jandcode.commons.*
import jandcode.jc.*

/**
 * Утилиты для поддержки компилирования java+groovy
 */
class JavaCompiler extends ProjectScript {

    public static final String LIB_GROOVY = "groovy";

    /**
     * Компиляция java исходников.
     * Если в исходниках встречаются groovy файлы, то они тоже прозрачно компиляются.
     *
     * @param destdir
     *    Куда помещать скомпилированные файлы
     *
     * @param srcs
     *    Каталоги с исходными файлами
     *
     * @param libs
     *    Библиотеки
     *
     * @param debug
     *    Включать ли отладочную информацию
     *
     * @param exclude_res
     *    Игнорируемые ресурсы при сборке. Все остальные файлы из каталогов с
     *    исходниками включаются в выходной каталог
     *
     * @param classpath : String
     *    Дополнительный classpath, используется как добавка к libs (например при компилировании
     *    тестов в путь должны включаться только что скомпилированные исходники модуля)
     *
     * @param encoding
     *    Кодировка исходных файлов
     */
    public void compile(Map pp) {
        def p = ut.asVariantMap(pp)
        //
        String destdir = p.getString('destdir', wd("temp/compiled"))
        List srcs = p.get('srcs', [wd("src")])
        List libs = p.get('libs', [])
        boolean debug = p.get('debug', true)
        List exclude_res = p.get('exclude_res', ["**/*.java", "**/*.groovy"])
        String encoding = p.get('encoding', "utf-8")
        String classpath = p.get('classpath', "")
        //

        ctx.classpath("jdk-tools")

        log.debug("Compile to [${destdir}]")
        ut.cleandir(destdir)

        // библиотеки
        def rlibs = ctx.getLibs(libs)

        // определяем наличие groovy
        boolean isGroovy = hasGroovy(srcs)
        if (isGroovy) {
            log.debug "GROOVY compile mode on"
            // включаем groovy, если ее нет
            if (rlibs.find(LIB_GROOVY) == null) {
                error("В исходниках имеюются groovy-файлы, но библиотека ${LIB_GROOVY} не указана в зависимостях")
            }
            rlibs.add(ctx.getLib("groovy-ant"))
        }

        def cp = rlibs.classpath
        if (!UtString.empty(classpath)) {
            cp.addAll(UtCnv.toList(classpath))
        }
        String cp_s = UtString.join(cp, ";")

        if (log.verbose) {
            log.debug(ut.makePrintClasspath(cp))
        }

        if (!isGroovy) {
            ant.javac(destdir: destdir, debug: debug, includeantruntime: false, fork: true,
                    classpath: cp_s, encoding: encoding) {
                for (s in srcs) {
                    src(path: s)
                }
                include(name: "**/*.java")
            }
        } else {
            ant.taskdef(name: "groovyc", classname: "org.codehaus.groovy.ant.Groovyc")
            ant.groovyc(destdir: destdir, includeantruntime: false, fork: true,
                    classpath: cp_s, encoding: encoding) {
                for (s in srcs) {
                    src(path: s)
                }
                javac(debug: debug, encoding: encoding)
            }

        }
        if (!exclude_res.contains('**/*')) {
            ant.copy(todir: destdir, overwrite: "true", preservelastmodified: true) {
                for (s in srcs) {
                    fileset(dir: s) {
                        include(name: "**/*")
                        for (ex in exclude_res) {
                            exclude(name: ex)
                        }
                    }
                }
            }
        }

    }

    /**
     * Есть ли groovy-файлы в указанных каталогах
     */
    boolean hasGroovy(List srcs) {
        for (src in srcs) {
            def scanner = ant.fileset(dir: src, includes: "**/*.groovy")
            for (ff in scanner) {
                return true
            }
        }
        return false
    }

}
