package jandcode.jc.impl.lib;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.jc.*;

import java.text.*;
import java.util.*;

public class LibHolder implements ILibs {

    /**
     * Переменная в project.vars, которая содержит true, если проект в classpath
     */
    public static final String VAR_LIB_IN_CLASSPATH = LibHolder.class.getName() + "-LibInClasspath";

    private Ctx ctx;
    private List<ILibProvider> libProviders = new ArrayList<ILibProvider>();
    private Set<String> usedInClasspath = new HashSet<>();

    public LibHolder(Ctx ctx) {
        this.ctx = ctx;
        // системные библиотеки
        addLibProvider(new LibProviderSys(ctx));
        //
        ctx.onEvent(JcConsts.Event_ProjectAdded.class, e -> {
            Project project = e.getProject();
            String libpath = project.wd(JcConsts.LIB_DIR);
            if (UtFile.exists(libpath)) {
                addLibProviderFromPath(libpath);
            }
        });
    }

    private void addLibProviderFromPath(String libpath) {
        ILibProvider p = new LibProviderStd(ctx, libpath);
        addLibProvider(p);
    }

    ////// ILibs

    public void addLibProvider(ILibProvider p) {
        ctx.getLog().debug(MessageFormat.format("add lib provider [{0}]", p));
        // чем позже добавлена, тем выше приоритет
        libProviders.add(0, p);

        // загружаем библиотеки!
        p.getLibs();

        // уведомляем
        ctx.fireEvent(new JcConsts.Event_LibProviderAdded(p));
    }

    public List<ILibProvider> getLibsProviders() {
        return new ArrayList<>(this.libProviders);
    }

    public Lib findLib(String name) {
        for (ILibProvider p : libProviders) {
            Lib z = p.findLib(name);
            if (z != null) {
                return z;
            }
        }
        return findLibForModule(name);
    }

    public Lib getLib(String name) {
        Lib z = findLib(name);
        if (z == null) {
            throw new XError("Library [{0}] not found", name);
        }
        return z;
    }

    private void addWithDep(ListLib res, String name, List<String> groupNames) {
        Lib lib1 = getLib(name);
        if (res.contains(lib1.getName())) {
            return; // уже в списке
        }
        for (String gn : groupNames) {
            LibDependsGroup g = lib1.getDepends().getGroups().find(gn);
            if (g == null) {
                continue;
            }
            for (Lib depLib : g.getLibs()) {
                addWithDep(res, depLib.getName(), groupNames);
            }
        }
        if (!res.contains(lib1.getName())) {
            // так и не попала в список
            res.add(lib1);
        }
    }

    public ListLib getLibs(Object libNames, String groupNames) {
        List<String> gnms = UtCnv.toList(groupNames);
        if (gnms.size() == 0) {
            gnms.add(JcConsts.EXPAND_DEPENDS_DEFAULT);
        }
        List<String> libnms = UtCnv.toNameList(libNames);
        ListLib res = new ListLib();
        //
        for (String libName : libnms) {
            addWithDep(res, libName, gnms);
        }
        //
        return res;
    }

    public ListLib getLibs(Object names) {
        return getLibs(names, JcConsts.EXPAND_DEPENDS_DEFAULT);
    }

    public ListLib getLibs() {
        ListLib tmp = new ListLib();
        for (ILibProvider p : libProviders) {
            List<Lib> lst1 = p.getLibs();
            for (Lib z : lst1) {
                if (tmp.find(z.getName()) != null) {
                    continue;
                }
                tmp.add(z);
            }
        }
        return tmp;
    }

    protected void addClasspath(Lib lib) {
        if (usedInClasspath.contains(lib.getName())) {
            return; // ужу включали
        }
        usedInClasspath.add(lib.getName());
        //
        String cp = lib.getClasspath();
        if (UtString.empty(cp)) {
            return;
        }
        if (UtClass.addClasspath(cp)) {
            ctx.debug(MessageFormat.format("add classpath [{0}]", cp));
        }
        if (lib.getSourceProject() != null) {
            // проект в исходниках, ставим ему метку, что он в classpath
            lib.getSourceProject().getVars().put(VAR_LIB_IN_CLASSPATH, true);
        }
    }

    //////

    public void classpath(Object libs) {
        ListLib rlibs = ctx.getLibs(libs);
        for (Lib lib : rlibs) {
            addClasspath(lib);
        }
    }

    public ListLib loadLibs(String path) {
        LibProviderStd pr = new LibProviderStd(ctx, path);
        return pr.getLibs();
    }

    public void addLibDir(String path) {
        addLibProviderFromPath(path);
    }

    public Lib findLibForModule(String moduleName) {
        for (ILibProvider p : libProviders) {
            for (Lib z : p.getLibs()) {
                for (String mn : z.getModules()) {
                    if (mn.equals(moduleName)) {
                        return z;
                    }
                }
            }
        }
        return null;
    }

}
