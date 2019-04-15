package jandcode.jc.std

import jandcode.commons.*
import jandcode.commons.error.*
import jandcode.jc.*
import jandcode.jc.impl.just.*

/**
 * Сборщик продукта.
 * Базовый класс, предназначен для расширения.
 */
class ProductBuilder extends ProjectScript {

    /**
     * Имя сборщика. Опционально.
     * Если указано, то такой сборщик можно найти по имени
     * методом {@link jandcode.jc.std.ProductBuilder#getBuilder(java.lang.String)}.
     * Используется в проектах с несколькими сборщиками продукта.
     */
    String name

    /**
     * При значении true в командной строке передан параметр -q
     * и собирать нужно быстро. Например - пропускать компиляцию проекта,
     * т.к. в предыдущем запуске она уже выполнена.
     * Используется при отладке сборщика.
     */
    boolean quick

    /**
     * При значении true в командной строке передан параметр -dev
     * и собирать нужно в режиме разработки.
     */
    boolean dev

    /**
     * При значении true проверяется версия предыдущей сборки.
     * Если она не изменилась,сборка не проводится
     */
    boolean update

    /**
     * Аргументы командной строки, переданной при выполнеии команды product
     */
    CmArgs args = new CmArgsImpl()

    //////

    private String _destDir = "_jc/product"

    /**
     * Куда собираем продукт.
     * Если в проекте несколько сборщиков, нужно каждому указать индивидуальный
     * каталог.
     */
    String getDestDir() {
        return wd(this._destDir)
    }

    void setDestDir(String destDir) {
        this._destDir = destDir
    }

    //////

    protected void onInclude() throws Exception {
        // добавляем команду product автоматом
        include(ProductProject)
    }

    //////

    /**
     * Выполнить сборку
     */
    void exec() {
        //
        String nm = name == null ? "" : " [${name}]"
        log.info("start product builder${nm}")

        if (update) {
            boolean needBuild = true

            String versionFile = "${destDir}/VERSION"
            if (UtFile.exists(versionFile)) {
                String curVer = UtFile.loadString(versionFile).trim()
                if (curVer == project.version.toString()) {
                    needBuild = false
                }
            }

            if (!needBuild) {
                log.info("Already exists product${nm} version ${project.version} in [${destDir}]")
                return
            }
        }

        ut.cleandir(destDir)
        //
        onExec()
        //
        log.info("product${nm} version ${project.version} in [${destDir}]")
    }

    /**
     * Реализация сборки проекта
     */
    void onExec() {
    }

    //////

    /**
     * Получить другой builder по имени.
     * Например нужно собрать почти такой же продукт, но с некоторыми отличиями.
     * Получаем builder, копируем его сборку и изменяем.
     */
    ProductBuilder getBuilder(String name) {
        ProductBuilder b = include(ProductProject).builders.get(name)
        if (b == null) {
            throw new XError("Не найден ProductBuilder: ${name}")
        }
        return b
    }

    ////// утилиты

    /**
     * Собрать проект, если quick=false
     */
    void buildProject() {
        if (!this.quick) {
            cm.exec("build")
        }
    }

    /**
     * Скопировть содержимое папки в каталог destDir
     */
    void copyFolderToDestDir(String folder, boolean required = false) {
        String p1 = wd(folder)
        if (!required && !UtFile.exists(p1)) {
            return
        }
        ant.copy(todir: destDir, overwrite: true, preservelastmodified: true) {
            fileset(dir: p1)
        }
    }

    /**
     * Создать копировальщик библиотек
     */
    LibCopier createLibCopier(boolean createLibXml = false) {
        LibCopier cp = create(LibCopier)
        cp.createLibXml = createLibXml
        return cp
    }

    /**
     * Создать файл VERSION в корне продукта
     */
    void makeVersionFile() {
        ant.echo(message: project.version, file: destDir + "/VERSION")
    }

    /**
     * Создать файл .jc-root в корне продукта
     */
    void makeJcRootFile() {
        ant.echo(message: "", file: destDir + "/${JcConsts.JC_ROOT_FILE}")
    }

}
