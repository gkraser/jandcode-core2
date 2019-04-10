package jandcode.web.tst;

import jandcode.commons.*;
import jandcode.core.*;
import jandcode.web.*;
import jandcode.web.virtfile.*;

import java.util.*;

/**
 * Ресурс для tst
 */
public class TstRes implements Comparable<TstRes> {

    private App app;
    private List<TstRes> childs;
    private String path;
    private boolean tstRoot;
    private boolean folder;
    private ITstResFilter resFilter;

    /**
     * Возвращает список tst-корней
     */
    public static List<String> getTstRoots(WebService svc) {
        //
        List<String> res = new ArrayList<String>();
        for (Mount mount : svc.getMounts()) {

            if (mount.getVirtualPath().equals("jandcode/web/tst")) {
                continue;  // собственный не нужен
            }

            // есть ли папка tst внутри примонтированной
            String vp = mount.getVirtualPath() + "/_tst";
            VirtFile f = svc.findFile(vp);
            if (f != null && f.isFolder()) {
                res.add(f.getPath());
                continue;
            }

            // а не явно ли примонтирован tst
            if (!UtString.empty(mount.getVirtualPath()) && (mount.getVirtualPath().endsWith("/_tst") || mount.getVirtualPath().equals("_tst"))) {
                vp = mount.getVirtualPath();
                f = svc.findFile(vp);
                if (f != null && f.isFolder()) {
                    res.add(f.getPath());
                }
            }

        }

        return res;
    }

    public static TstRes createRoot(App app, String path, boolean tstOnly, ITstResFilter resFilter) {
        //
        WebService svc = app.bean(WebService.class);

        if (resFilter == null) {
            resFilter = app.create(TstIgnoreFile.class);
        }

        TstRes root;

        if (UtString.empty(path)) {
            // пустой путь
            // собираем все явные каталоги tst из всех подключенных ресурсов
            root = new TstRes(app, null, true, resFilter);
            Set<String> lst = new LinkedHashSet<>();
            lst.addAll(getTstRoots(svc));

            for (String s : lst) {
                TstRes z = new TstRes(app, s, true, resFilter);
                if (z.getChilds().size() == 0) {
                    continue;
                }
                root.getChilds().add(z);
            }
            if (!tstOnly) {
                root.getChilds().add(new TstRes(app, "/", true, resFilter));
            }

        } else if ("ROOT".equals(path)) {
            root = new TstRes(app, "/", true, resFilter);

        } else {
            root = new TstRes(app, path, true, resFilter);
        }
        return root;
    }

    TstRes(App app, String path, boolean folder, ITstResFilter resFilter) {
        this.app = app;
        this.folder = folder;
        this.resFilter = resFilter;
        if (this.resFilter == null) {
            this.resFilter = app.create(TstIgnoreFile.class);
        }
        if (path != null) {
            this.path = UtVDir.normalize(path);
        } else {
            // это ручкосборный каталог
            this.tstRoot = true;
            this.childs = new ArrayList<TstRes>();
            this.path = "";
        }
    }

    public List<TstRes> getChilds() {
        if (childs != null) {
            return childs;
        }

        //
        childs = new ArrayList<>();
        WebService svc = app.bean(WebService.class);

        Collection<VirtFile> tmp = svc.findFiles(path);
        for (VirtFile f : tmp) {
            if (resFilter.isIgnore(f)) {
                continue;
            }
            TstRes r = new TstRes(app, f.getPath(), f.isFolder(), this.resFilter);
            childs.add(r);
        }

        Collections.sort(childs);

        return childs;
    }

    public int compareTo(TstRes o) {
        TstRes o1 = this;
        TstRes o2 = o;
        if (o1.isFolder() == o2.isFolder()) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
        return o1.isFolder() ? -1 : 1;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return UtFile.filename(path);
    }

    public boolean isTstRoot() {
        return tstRoot;
    }

    public boolean isFolder() {
        return folder;
    }

    public boolean isGsp() {
        VirtFile f = app.bean(WebService.class).findFile(path);
        if (f == null || f.isFolder()) {
            return false;
        }
        return "gsp".equals(f.getFileType());
    }

    /**
     * Сколько строк будет, если число колонок cols
     */
    public int childRows(int cols) {
        int c = getChilds().size() / cols;
        if (getChilds().size() % cols > 0) {
            c++;
        }
        if (c == 0) {
            c = 1;
        }
        return c;
    }

    /**
     * Элемент в указанной ячейке при указанном количестве колонок. Может быть null.
     * Нумерация с 0
     */
    public TstRes childCell(int row, int col, int countCols) {
        int rows = childRows(countCols);
        int idx = col * rows + row;
        if (idx >= getChilds().size()) {
            return null;
        }
        return getChilds().get(idx);
    }

}
