package jandcode.core.dbm.sql.impl;

import jandcode.commons.*;
import jandcode.commons.conf.*;
import jandcode.commons.groovy.*;
import jandcode.core.dbm.*;
import jandcode.core.groovy.*;
import org.apache.commons.vfs2.*;

/**
 * Утилиты для загрузки sql из conf
 */
public class SqlConfUtils {

    /**
     * Предок для генератора sql
     */
    public abstract static class SqlGenGspTemplate extends SimpleGspTemplate implements IModelLink, IConfLink {

        private Model model;
        private Conf conf;

        public Model getModel() {
            return model;
        }

        public void setModel(Model model) {
            this.model = model;
        }

        public Conf getConf() {
            return conf;
        }

        public void setConf(Conf conf) {
            this.conf = conf;
        }
    }

    /**
     * Загрузить sql из указанного узла conf.
     * <p>
     * Как работает:
     * <p>
     * Если есть атрибут file.DBTYPE, то берем этот файл.
     * Если есть атрибут file, то берем этот файл.
     * <p>
     * Если файл указан, то либо берем его содержимое (любое расширение, кроме gsp),
     * либо генерируем текст по gsp, через шаблон {@link SimpleGspTemplate},
     * куда объект context и передаем.
     * <p>
     * Если файл не указан, то берем текст из атрибутов text.DBTYPE или text.
     *
     * @param conf  из какого узла
     * @param model в контексте какой модели
     * @return текст sql
     */
    public static String loadSqlTextFromRt(Conf conf, Model model) throws Exception {
        String dbType = model.getDbType();

        String res = "";

        // сначала файл
        String fn = conf.getString("file." + dbType);
        if (UtString.empty(fn)) {
            fn = conf.getString("file");
        }
        if (!UtString.empty(fn)) {
            String ext = UtFile.ext(fn);
            if ("gsp".equals(ext)) {
                // gsp, генерируем
                FileObject f = UtFile.getFileObject(fn);
                GroovyCompiler gc = model.getApp().bean(GroovyService.class).getGroovyCompiler(Model.class.getName());
                GroovyClazz gcls = gc.getClazz(SqlGenGspTemplate.class, "void doGenerate()", f, true);
                SqlGenGspTemplate t = (SqlGenGspTemplate) gcls.createInst();
                t.setModel(model);
                t.setConf(conf);
                res = t.generate(model);
            } else {
                // другой файл - загружаем
                res = UtFile.loadString(fn);
            }
        } else {
            // файла нет, текст тегов
            res = conf.getString("text." + dbType);
            if (UtString.empty(res)) {
                res = conf.getString("text");
            }
            res = UtString.normalizeIndent(res);
        }

        //
        res = res.trim();
        return res;
    }


}
