package jandcode.commons.source;

import jandcode.commons.named.*;

import java.util.*;

/**
 * Директива '//#jc NAME [PARAM1[,PARAM_n..]' для исходных текстов.
 * Метод getName() возвращает имя директивы.
 */
public class JcDirective extends Named {

    private List<String> params = new ArrayList<>();

    /**
     * Создать директиву
     *
     * @param text текст директивы без '//#jc'
     */
    public JcDirective(String text) {
        text = text.trim();

        int a = text.indexOf(' ');
        if (a == -1) {
            this.name = text.toLowerCase();
        } else {
            this.name = text.substring(0, a).toLowerCase();
            String s = text.substring(a + 1);
            String[] e = s.split(",");
            for (int i = 0; i < e.length; i++) {
                params.add(e[i].trim());
            }
        }
    }

    /**
     * Все параметры директивы
     */
    public List<String> getParams() {
        return params;
    }

    /**
     * Первый параметр. Если параметров нет, возвращается пустая строка
     */
    public String getParam0() {
        if (params.size() > 0) {
            return params.get(0);
        }
        return "";
    }

}
