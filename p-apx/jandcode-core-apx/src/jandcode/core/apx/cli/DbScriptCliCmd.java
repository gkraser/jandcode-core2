package jandcode.core.apx.cli;

import groovy.lang.*;
import jandcode.commons.*;
import jandcode.commons.attrparser.*;
import jandcode.commons.cli.*;
import jandcode.commons.error.*;
import jandcode.commons.groovy.*;
import jandcode.core.cli.*;
import jandcode.core.dbm.mdb.*;
import jandcode.core.dbm.std.*;
import jandcode.core.groovy.*;

import java.util.*;

/**
 * Команда cli: выполнение скрипта в контексте базы данных.
 * <p>
 * Скрипт - это скрипт groovy, наследник от {@link BaseScript}. Текст скрипта - это тело
 * метода onRun.
 * Имеется свойство mdb {@link Mdb} с установленным соединением.
 * Транзакция стартована.
 * <p>
 * Пример скрипта:
 *
 * <pre>{@code
 * st = mdb.loadQuery("select * from StatusType order by code")
 * mdb.outTable(st)
 * }</pre>
 * <p>
 * Если хочется подсказок от idea для mdb, можно написать так:
 *
 * <pre>{@code
 * import jandcode.core.dbm.mdb.*
 * Mdb mdb = this.mdb
 * //
 * def st = mdb.loadQuery("select * from StatusType order by code")
 * mdb.outTable(st)
 * }</pre>
 */
public class DbScriptCliCmd extends BaseAppCliCmd {

    public void exec() throws Exception {
        CliDbTools dbTools = new CliDbTools(getApp(), getModelName());
        if (isShowInfo()) {
            dbTools.showInfo();
        }
        String script = null;
        if (!UtString.empty(getScriptFile())) {
            script = UtFile.loadString(getScriptFile());
        } else {
            throw new XError("Не указан параметр -f");
        }
        //
        Mdb mdb = dbTools.getModel().createMdb();
        //
        GroovyCompiler gc = getApp().bean(GroovyService.class).getGroovyCompiler(DbScriptCliCmd.class.getName());
        GroovyClazz gcls = gc.getClazz(BaseScript.class, "void doRun()", script, false);
        BaseScript t = (BaseScript) gcls.createInst();
        t.setMdb(mdb);
        for (var v : getVars().entrySet()) {
            t.getBinding().setVariable(v.getKey(), v.getValue());
        }
        //
        mdb.connect();
        try {
            mdb.startTran();
            try {
                t.run();
                if (mdb.isTran()) {
                    mdb.commit();
                }
            } catch (Exception e) {
                if (mdb.isTran()) {
                    mdb.rollback(e);
                } else {
                    throw new XErrorWrap(e);
                }
            }
        } finally {
            mdb.disconnect();
        }
    }

    public void cliConfigure(CliDef b) {
        b.desc("Выполнение скрипта в контексте соединения с базой данных");
        b.opt("modelName")
                .names("-m").arg("MODEL")
                .desc("Для какой модели")
                .defaultValue("default");
        b.opt("showInfo")
                .names("-i")
                .desc("Показать настройки базы перед выполнением");
        b.opt("scriptFile")
                .names("-f").arg("FILE")
                .desc("Файл со скриптом");
        b.opt("vars")
                .names("-d").arg("name=value")
                .multi(true)
                .desc("Переменная с именем name и строковым значением value для использования в скрипте.\nМожно указывать несколько раз");
    }

    //////

    /**
     * Предок для скриптов
     */
    public abstract static class BaseScript extends Script {

        private Mdb mdb;

        public Mdb getMdb() {
            return mdb;
        }

        public void setMdb(Mdb mdb) {
            this.mdb = mdb;
        }

        public Object run() {
            try {
                doRun();
            } catch (Exception e) {
                throw new XErrorWrap(e);
            }
            return null;
        }

        /**
         * Реализация метода run. Текст скрипта является телом этого метода.
         */
        protected abstract void doRun() throws Exception;

    }


    private String modelName = "default";
    private boolean showInfo = false;
    private String scriptFile = null;
    private Map<String, String> vars = new HashMap<>();

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public boolean isShowInfo() {
        return showInfo;
    }

    public void setShowInfo(boolean showInfo) {
        this.showInfo = showInfo;
    }

    public String getScriptFile() {
        return scriptFile;
    }

    public void setScriptFile(String scriptFile) {
        this.scriptFile = scriptFile;
    }

    public Map<String, String> getVars() {
        return vars;
    }

    public void setVars(List<String> vars) {
        if (vars == null) {
            return;
        }
        this.vars.clear();
        for (String v : vars) {
            AttrParser ap = new AttrParser();
            ap.loadFrom(v);
            this.vars.putAll(ap.getResult());
        }
    }

}
