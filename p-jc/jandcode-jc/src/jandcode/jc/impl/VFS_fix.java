package jandcode.jc.impl;

import jandcode.commons.error.*;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.impl.*;

import java.lang.reflect.*;

/**
 * fix для "Не найден файл res:jandcode/app/module.cfx"
 */
public class VFS_fix {

    private static boolean fixed = false;

    public static void doFix() {
        if (fixed) {
            return;
        }
        try {
            Field fld = VFS.class.getDeclaredField("instance");
            fld.setAccessible(true);
            StandardFileSystemManager fm = new StandardFileSystemManager();
            fm.setCacheStrategy(CacheStrategy.ON_RESOLVE);
            fm.init();
            fld.set(VFS.class, fm);
        } catch (Exception e) {
            throw new XErrorWrap(e);
        }
        //
        fixed = true;
    }

}
