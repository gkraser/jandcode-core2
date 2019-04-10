package jandcode.core.std.impl;

import jandcode.commons.*;
import jandcode.commons.vdir.*;
import jandcode.core.*;
import jandcode.core.std.*;
import org.apache.commons.vfs2.*;

import java.util.*;

public class JcDataAppServiceImpl extends BaseComp implements JcDataAppService {

    private VDirWrap vdir = new VDirWrap(new VDirVfs());

    protected void onConfigure(BeanConfig cfg) throws Exception {
        super.onConfigure(cfg);
        //

        for (Module module : getApp().getModules()) {
            String path = null;
            if (module.getSourceInfo().isSource()) {
                // в исходниках
                String p = UtFile.join(module.getSourceInfo().getProjectPath(), JcDataAppService.JC_DATA_DIR);
                if (UtFile.exists(p)) {
                    path = UtFile.getFileObject(p).toString();
                }
            } else {
                // в jar
                String p = module.getPath();
                String[] parts = module.getPackageRoot().split("\\.");
                String s = "";
                for (int i = 0; i < parts.length; i++) {
                    s += "/..";
                }
                p += s + "/" + JcDataAppService.JC_DATA_RESOURCE;
                FileObject f = UtFile.getFileObject(p);
                if (f.exists()) {
                    path = f.toString();
                }
            }
            if (path != null) {
                getVdir().addRoot(path);
            }
        }

    }

    public VDir getVdir() {
        return vdir.getVdir();
    }

    public String findFile(String path) {
        return vdir.findFile(path);
    }

    public String getFile(String path) {
        return vdir.getFile(path);
    }

    public List<String> findFiles(String path) {
        return vdir.findFiles(path);
    }

}
