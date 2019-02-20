package jandcode.xcore.std.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.impl.*;
import jandcode.commons.named.*;
import jandcode.xcore.*;
import jandcode.xcore.std.*;

public class DataDirServiceImpl extends BaseComp implements DataDirService {

    public static final String DEFAULT_PATH = "${root}/${name}";

    protected NamedList<PathItem> paths = new DefaultNamedList<>("Не найден path [{0}] в datadir");

    protected class VarExpander extends SubstVarParser {

        private String name;

        VarExpander(String name) {
            this.name = name;
        }

        public String onSubstVar(String v) {
            if ("tempdir".equals(v)) {
                return UtFile.getTempdir();

            } else if ("appdir".equals(v)) {
                return getApp().getAppdir();

            } else if ("appdir.key".equals(v)) {
                return UtString.md5Str(getApp().getAppdir());

            } else if ("workdir".equals(v)) {
                return getApp().getWorkdir();

            } else if ("workdir.key".equals(v)) {
                return UtString.md5Str(getApp().getWorkdir());

            } else if ("homedir".equals(v)) {
                return UtFile.getHomedir();

            } else if ("appname".equals(v)) {
                return getApp().getAppName();

            } else if ("name".equals(v)) {
                return name;

            } else {
                return paths.get(v).getPath();

            }
        }
    }


    class PathItem extends Named {
        String pathFromIni;
        String pathReal;

        PathItem(String name, String pathFromIni) {
            setName(name);
            this.pathFromIni = pathFromIni;
        }

        public String getPath() {
            if (pathReal == null) {
                synchronized (this) {
                    if (pathReal == null) {
                        VarExpander ex = new VarExpander(getName());
                        ex.loadFrom(pathFromIni);
                        String tmp = UtFile.abs(ex.getResult());
                        if (!UtFile.exists(tmp)) {
                            UtFile.mkdirs(tmp);
                        }
                        pathReal = tmp;
                    }
                }
            }
            //
            return pathReal;
        }

        public String getPath(String localPath) {
            String p = UtFile.join(getPath(), localPath);
            if (!UtFile.exists(p)) {
                UtFile.mkdirs(p);
            }
            return p;
        }

    }

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //
        for (Conf r1 : getApp().getConf().getConfs("datadir/path")) {
            addPathItem(r1);
        }
    }

    protected void addPathItem(Conf conf) {
        String name = conf.getName();
        String path = "";
        if (getApp().isDebug()) {
            path = conf.getString("path.debug");
        }
        if (UtString.empty(path)) {
            path = conf.getString("path");
        }
        if (UtString.empty(path)) {
            path = DEFAULT_PATH;
        }
        PathItem it = new PathItem(name, path);
        paths.add(it);
    }

    //////

    public String getPath(String name) {
        return paths.get(name).getPath();
    }

    public String getPath(String name, String localPath) {
        return paths.get(name).getPath(localPath);
    }

}
