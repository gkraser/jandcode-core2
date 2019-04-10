package jandcode.jc.impl.lib

import jandcode.commons.*
import jandcode.commons.error.*
import jandcode.commons.named.*
import jandcode.commons.simxml.*
import jandcode.commons.variant.*
import jandcode.jc.*
import jandcode.jc.impl.depends.*
import jandcode.jc.std.*

/**
 * Загрузка библиотек из каталога
 */
public class LibDirLoader {

    private Ctx ctx;
    private String libPath;
    private NamedList<LibInfo> libInfos;
    private Set<String> usedFiles;

    //
    private class LibInfo extends Named {

        String version;
        String jar;
        String src;
        LibDepends depends;
        String artifactId;
        String groupId;

        private LibInfo(String name) {
            this.name = name;
        }

        //////

        public String getVersion() {
            return version;
        }

        //////

        void setJar(String path) {
            if (path.length() == 0) {
                jar = "";
                return;
            }
            path = UtFile.abs(path);
            usedFiles.add(path);
            this.jar = path;
        }

        void setSrc(String path) {
            if (path.length() == 0) {
                src = "";
                return;
            }
            path = UtFile.abs(path);
            usedFiles.add(path);
            this.src = path;
        }

        void applyXml(SimXml x, String basePath) {
            // если описание в xml, то оно должно быть полным. и depends значит определен
            this.depends = new LibDependsImpl(ctx, name)
            //
            IVariantMap attrs = x.getAttrs();
            for (String an : attrs.keySet()) {
                String av = attrs.getString(an);
                if ("version".equals(an)) {
                    version = av;
                } else if ("artifactId".equals(an)) {
                    artifactId = av
                } else if ("groupId".equals(an)) {
                    groupId = av
                } else if ("jar".equals(an)) {
                    if (!UtFile.isAbsolute(av)) {
                        av = UtFile.join(basePath, av)
                    }
                    jar = av
                } else if ("src".equals(an)) {
                    if (!UtFile.isAbsolute(av)) {
                        av = UtFile.join(basePath, av)
                    }
                    src = av
                } else {
                    String depGroup = UtString.removePrefix(an, "depends.")
                    if (depGroup != null) {
                        depends.addGroup(depGroup).add(UtString.toList(av));
                    }
                }
            }
        }

        public Lib createLib() {
            return new LibJar(ctx, name, version, depends, jar, src, artifactId, groupId);
        }

    }

    //////

    public LibDirLoader(Ctx ctx, String libPath) {
        this.ctx = ctx;
        this.libPath = libPath;
    }

    public List<Lib> load() {
        if (!UtFile.exists(libPath)) {
            return new ArrayList<Lib>()
        }

        libInfos = new DefaultNamedList<LibInfo>();
        usedFiles = new HashSet<String>();

        // все файлы
        List<File> lst

        // сначала jc
        def sc = UtFile.createDirScanner(libPath)
        sc.include("**/*.jc")
        sc.exclude("**/temp")
        sc.exclude("**/.*")
        def scLst = sc.load()

        lst = new ArrayList()
        for (f in scLst) {
            lst.add(f)
        }

        // формируем список каталогов: собственно lib и все, которые вернет jc
        def dirLst = [libPath]
        dirLst.addAll(loadJc(lst));

        // потом все остальное
        lst = new ArrayList()
        for (dir in dirLst) {

            sc = UtFile.createDirScanner(dir)
            sc.exclude("**/*.jc")
            sc.exclude("**/temp")
            sc.exclude("**/.*")
            scLst = sc.load()

            for (f in scLst) {
                lst.add(f)
            }
        }

        //
        loadJar(lst);
        loadSrc(lst);
        loadLibXml(lst);

        //
        List<Lib> res = new ArrayList<Lib>();
        for (LibInfo libInfo : libInfos) {
            res.add(libInfo.createLib());
        }
        return res;
    }

    //////

    protected LibInfo getLibInfo(String name) {
        LibInfo res = libInfos.find(name);
        if (res == null) {
            res = new LibInfo(name);
            libInfos.add(res);
        }
        return res;
    }

    protected void loadLibXml(List<File> fs) throws Exception {
        for (File file : fs) {
            String name = file.getName();
            String basePath = file.getParentFile().getAbsolutePath();
            if (!name.endsWith(".lib.xml")) {
                continue;
            }
            SimXml x1 = new SimXmlNode();
            x1.load().fromFile(file);

            for (SimXml x : x1.getChilds()) {
                if (x.hasName("lib")) {
                    String libName = x.getAttrs().getString("name");
                    if (UtString.empty(libName)) {
                        throw new XError("Атрибут name не указан для узла [{0}] в файле [{1}]",
                                x.save().toString(), file.getAbsolutePath());
                    }
                    LibInfo libInfo = libInfos.find(libName);
                    if (libInfo == null) {
                        throw new XError("Библиотека [{0}] не имеет jar-файла. см: [{1}]",
                                libName, file.getAbsolutePath());
                    }
                    libInfo.applyXml(x, basePath);
                }
            }
        }

    }

    protected void loadJar(List<File> fs) throws Exception {
        for (File file : fs) {
            if (usedFiles.contains(file.getAbsolutePath())) {
                continue;
            }
            String name = file.getName();
            String ext = UtFile.ext(name);

            if ("jar".equals(ext)) {

                if (name.endsWith("-src.jar") || name.endsWith("-source.jar") || name.endsWith("-sources.jar")) {
                    continue;
                }

                name = LibUtils.filenameToLibname(name);

                LibInfo libInfo = getLibInfo(name);
                libInfo.setJar(file.getAbsolutePath());
            }
        }
    }

    protected void loadSrc(List<File> fs) throws Exception {
        for (File file : fs) {
            if (usedFiles.contains(file.getAbsolutePath())) {
                continue;
            }
            String name = file.getName();

            if (name.endsWith("-src.zip") || name.endsWith("-src.jar") || name.endsWith("-source.jar") || name.endsWith("-sources.jar")) {
                name = LibUtils.filenameToLibname(name);
                LibInfo libInfo = libInfos.find(name);
                if (libInfo != null) {
                    libInfo.setSrc(file.getAbsolutePath());
                }
            }
        }
    }

    /**
     * Загрузка из jc.
     * Возвращает список полных имен каталогов, которые дополнительно содержат
     * библиотеки, описанные в jc.
     */
    protected List<String> loadJc(List<File> fs) throws Exception {
        List<String> res = new ArrayList<>()
        for (File file : fs) {
            String name = UtFile.ext(file.getName())
            if (name != "jc") {
                continue
            }

            // jc-файл имеется в каталоге с библиотекой

            // загружаем проект
            ctx.debug("found jc file in lib dir: ${file.getAbsolutePath()}")
            Project p = ctx.load(file.getAbsolutePath())

            // проверяем, есть ли кто, ответственный за формирование библиотек
            for (b in p.impl(ILibDirBuilder)) {
                List lst = b.buildLibDir()
                if (lst != null) {
                    for (d in lst) {
                        ctx.debug("jc file [${file.getAbsolutePath()}] define libdir [${d}]")
                    }
                    res.addAll(lst)
                }
            }

        }
        return res
    }

}
