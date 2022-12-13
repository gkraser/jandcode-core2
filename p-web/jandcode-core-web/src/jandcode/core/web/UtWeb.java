package jandcode.core.web;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.datetime.*;
import jandcode.commons.io.*;
import jandcode.core.*;
import jandcode.core.web.virtfile.*;

import javax.servlet.http.*;
import java.net.*;
import java.time.*;
import java.util.*;

/**
 * Разные статические утилиты для web
 */
public class UtWeb {
    /**
     * Для форматирования и разбора даты и времени в унифицированном формате GMT для web
     */
    private static XDateTimeFormatter FORMATTER_DATETIME_GMT =
            UtDateTime.createFormatter("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);

    public static final String CONF_MOUNT_MODULE = "web/mount-module";
    public static final String PREFIX_ALL_MODULES = "[*]";


    public static class ComparatorVirtFileByName implements Comparator<VirtFile> {
        public int compare(VirtFile o1, VirtFile o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    //////

    /**
     * Перевод локальной даты (в текущей системной локали) в строку GMT (для http-заголовков)
     */
    public static String dateToStringGMT(XDateTime d) {
        return d.clearMSec().toZone(UtDateTime.ZONE_UTC).toString(FORMATTER_DATETIME_GMT);
    }

    /**
     * Перевод строки GMT в дату в текущей системной локали (для http-заголовков)
     *
     * @param s исходная строка
     * @return null, если неправильная строка
     */
    public static XDateTime stringToDateGMT(String s) {
        try {
            return FORMATTER_DATETIME_GMT.parseDateTime(s).toZone(UtDateTime.ZONE_UTC, ZoneId.systemDefault()).clearMSec();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Отсортировать список файлов по имени
     */
    public static void sortVirtFilesByName(List<VirtFile> lst) {
        Collections.sort(lst, new ComparatorVirtFileByName());
    }

    /**
     * Проверка на абсолютный url
     */
    public static boolean isAbsoluteUrl(String url) {
        if (UtString.empty(url)) {
            return false;
        }
        return url.indexOf("://") != -1;
    }

    /**
     * Адрес сервера для запроса
     */
    public static String getServerAddr(Request request) {
        HttpServletRequest r = request.getHttpRequest();
        String s = r.getScheme() + "://" +
                r.getServerName() +
                ":" +
                r.getServerPort();
        return s;
    }

    //////

    /**
     * Возвращает список модулей, которые примонтированы через web/mount-module
     */
    public static List<ModuleInst> getMountModules(App app) {
        List<ModuleInst> res = new ArrayList<>();
        for (ModuleInst module : app.getModules()) {
            if (isMountModule(module)) {
                res.add(module);
            }
        }
        return res;
    }

    /**
     * Возвращает список модулей, которые примонтированы через web/mount-module
     * начиная с указанного базового. Т.е. только базовый и все, от которых он зависит.
     *
     * @param app            для какого приложения
     * @param baseModuleName с какого модуля начинать
     * @param includeBase    включать ли сам baseModuleName в список
     */
    public static List<ModuleInst> getMountModules(App app, String baseModuleName, boolean includeBase) {
        List<ModuleInst> res = new ArrayList<>();

        ModuleSubHolder sh = app.getModules().createSubHolder();
        if (includeBase) {
            sh.add(baseModuleName);
        } else {
            ModuleInst m = app.getModules().get(baseModuleName);
            for (String depend : m.getDepends()) {
                sh.add(depend);
            }
        }

        for (ModuleInst module : sh) {
            if (isMountModule(module)) {
                res.add(module);
            }
        }
        return res;
    }

    /**
     * Примонтирован ли модуль через web/mount-module
     */
    public static boolean isMountModule(ModuleInst module) {
        Conf mmrt = module.getConf().findConf(CONF_MOUNT_MODULE);
        return (mmrt != null);
    }

    //////

    /**
     * Раскрыть пути с '*'.
     * <p>
     * Если путь начинается на '[*]' то путь после '[*]' будет искаться во всех
     * примонтированных модулях.
     * <p>
     * Если путь начинается на '[MODULE-NAME]' то путь после '[MODULE-NAME]' будет искаться во всех
     * примонтированных модулях начиная с MODULE-NAME и его зависимостях.
     *
     * @param path путь, возможно с '*'
     * @return список путей
     */
    public static List<String> expandPath(App app, String path) {
        List<String> res = new ArrayList<>();
        if (UtString.empty(path)) {
            return res;
        }
        WebService svc = app.bean(WebService.class);
        // пробуем путь: [*]/PATH
        String wm = UtString.removePrefix(path, PREFIX_ALL_MODULES);
        if (wm != null) {
            // путь начинается с [*], перебираем для всех модулей
            wm = UtVDir.normalize(wm);
            for (ModuleInst m : getMountModules(app)) {
                String p = UtVDir.join(m.getVPath(), wm);
                List<String> tmp = expandPath(app, p);
                for (String tmpPath : tmp) {
                    VirtFile f = svc.findFile(tmpPath);
                    if (f != null) {
                        res.add(tmpPath);
                    }
                }
            }
            return res;
        }

        // пробуем путь: [MODULE-NAME]
        if (path.startsWith("[")) {
            int b = path.indexOf(']');
            if (b != -1) {
                // путь начинается с [MODULE-NAME], перебираем для модуля и его зависимостей
                String mn = path.substring(1, b);
                String pt = UtVDir.normalize(path.substring(b + 1));
                for (ModuleInst m : getMountModules(app, mn, true)) {
                    String p = UtVDir.join(m.getVPath(), pt);
                    List<String> tmp = expandPath(app, p);
                    for (String tmpPath : tmp) {
                        VirtFile f = svc.findFile(tmpPath);
                        if (f != null) {
                            res.add(tmpPath);
                        }
                    }
                }
                return res;
            }
        }

        if (path.indexOf("*") == -1) {
            // не ищем, просто добавляем, возможно это не файл, а модуль node_modules!
            res.add(UtVDir.normalize(path));

        } else {
            DirScanner<VirtFile> ps = svc.createDirScanner(path);
            List<VirtFile> lst = ps.load();
            for (VirtFile f : lst) {
                res.add(f.getPath());
            }
        }
        return res;
    }

    /**
     * Раскрыть все пути из списка.
     * См: {@link UtWeb#expandPath(jandcode.core.App, java.lang.String)}
     */
    public static List<String> expandPath(App app, List<String> paths) {
        List<String> res = new ArrayList<>();
        for (String path : paths) {
            List<String> tmp = expandPath(app, path);
            res.addAll(tmp);
        }
        return res;
    }

    /**
     * Для виртуального пути определеят модуль, которому принадлежит файл.
     *
     * @return null, если нет модуля
     */
    public static ModuleInst findModuleForPath(App app, String path) {
        WebService svc = app.bean(WebService.class);
        //
        VirtFile vf = svc.findFile(path);
        if (vf == null) {
            return null;
        }
        String rp = vf.getRealPath();
        if (UtString.empty(rp)) {
            return null;
        }
        // Ищем модуль с самым длинным путем, внури которого
        // лежит искомый файл
        ModuleInst res = null;
        for (ModuleInst m : app.getModules()) {
            if (rp.startsWith(m.getPath() + "/")) {
                if (res != null) {
                    if (m.getPath().length() > res.getPath().length()) {
                        res = m;
                    }
                } else {
                    res = m;
                }
            }
        }
        //
        return res;
    }


    /**
     * Кодирование спецсимволов в url.
     * Кроме '/'!
     */
    public static String encodeUrl(String uri) throws Exception {
        String s = URLEncoder.encode(uri, UtString.UTF8);
        s = s.replace("%2F", "/");
        return s;
    }

    /**
     * Установить заголовок http для download файла.
     * Пытается поддерживать русские имена файлов
     *
     * @param request  для какого запроса
     * @param fileName имя файла, которым будет представляется ответ
     * @param fileSize размер файла, может быть null
     */
    public static void setHeaderDownload(Request request, String fileName, Long fileSize) throws Exception {
        if (!request.isContentTypeAssigned()) {
            request.setContentType("application/octet-stream");
        }
        String userAgent = request.getHttpRequest().getHeader("USER-AGENT");
        if (userAgent == null) {
            userAgent = "";
        } else {
            userAgent = userAgent.toLowerCase();
        }
        fileName = URLEncoder.encode(fileName, "UTF8");
        boolean isMozilla = userAgent.contains("mozilla") && userAgent.contains("gecko")
                && !userAgent.contains("applewebkit");
        if (isMozilla) {
            request.setHeader(
                    "Content-Disposition",
                    "attachment; filename*=\"utf-8'" + fileName + "\""
            );
        } else {
            request.setHeader(
                    "Content-Disposition",
                    "attachment; filename=\"" + fileName + "\""
            );
        }
        if (fileSize != null) {
            request.setHeader("Content-Length", UtCnv.toString(fileSize));
        }
    }

}
