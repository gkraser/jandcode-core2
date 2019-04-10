package jandcode.jsa.utils;


import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.simxml.*;
import jandcode.commons.variant.*;
import jandcode.core.*;
import jandcode.web.*;
import jandcode.web.virtfile.*;

import java.util.*;

/**
 * Генератор svg-иконок
 */
public class SvgIconGenerator extends BaseComp {

    private Set<String> paths = new LinkedHashSet<>();

    /**
     * Добавить виртуальный путь иконок.
     * Формат: {@link UtWeb#expandPath(jandcode.core.App, java.lang.String)}
     */
    public void add(String path) {
        List<String> lst = UtWeb.expandPath(getApp(), path);
        paths.addAll(lst);
    }

    public String generate() {
        WebService svc = getApp().bean(WebService.class);
        StringBuilder sb = new StringBuilder();
        Map<String, VirtFile> byNames = new LinkedHashMap<>();

        // выявляем иконки с перекрытием
        for (String p : paths) {
            VirtFile f = svc.findFile(p);
            if (f == null) {
                continue;
            }
            String nm = UtFile.removeExt(f.getName());
            byNames.put(nm, f);
        }

        // генерим
        sb.append("{\n");
        boolean first = true;
        for (Map.Entry<String, VirtFile> entry : byNames.entrySet()) {
            if (!first) {
                sb.append(",\n");
            } else {
                first = false;
            }
            appendIconFile(sb, entry.getKey(), entry.getValue());
        }
        sb.append("\n}");

        return sb.toString();
    }

    private void appendIconFile(StringBuilder sb, String nm, VirtFile f) {
        sb.append("'");
        sb.append(nm);
        sb.append("':'");

        //
        SimXml x = new SimXmlNode();
        try {
            x.load().fromFileObject(f.getFileObject());

            // чистим все атрибуты, кроме viewBox
            IVariantMap attrs = x.getAttrs();
            Object vb = attrs.get("viewBox");
            attrs.clear();
            if (vb != null) {
                attrs.put("viewBox", vb);
            }

            // пишем в строку без отступов и заголовков
            SimXmlSaver sv = new SimXmlSaver(x);
            sv.setUseIndent(false);
            sv.setOutputXmlHeader(false);
            String s = sv.save().toString();

            // делаем js-строку правильную
            s = s.replace('\n', ' ');
            s = s.replace('\r', ' ');
            s = s.replace("\'", "\\'");

            sb.append(s);

        } catch (Exception e) {
            throw new XErrorWrap(e);
        }

        sb.append("'");
    }

}
