package jandcode.commons.test;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.outtable.*;
import org.slf4j.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Утилиты для тестов
 */
public class UtilsTestSvc extends BaseTestSvc {

    /**
     * Если true - то логирование включается в setUp методе.
     * Иначе - логирование отключается в setUp.
     */
    public boolean logSetUp = false;

    /**
     * Конфигурация logback для логгирования в setUp методе.
     * Либо имя локального файла, либо строка с xml-конфигурацией.
     */
    public String logSetUpCfg = null;

    static {
        UtLog.logOff();
    }

    //////

    public void setUp() throws Exception {
        if (logSetUp) {
            logOn(logSetUpCfg);
        } else {
            logOff();
        }
    }

    //////

    /**
     * Заменяет
     * '#' на имя тестового класса
     * '%' - на имя тестового метода
     * '&' - на имя пакета тестового класса
     */
    public String replaceTestName(String s) {
        return s.replace("#", getTest().getClass().getSimpleName())
                .replace("%", getTest().getTestName())
                .replace("&", getTest().getClass().getPackage().getName());
    }

    //////

    /**
     * Возвращает каталог, в котором лежит скомпилированный класс теста
     */
    public String getTestPath() {
        String fn = getTestFile(getTest().getClass().getSimpleName() + ".class"); //NON-NLS
        File f = new File(fn);
        return f.getParentFile().getAbsolutePath();
    }

    /**
     * Возвращает полное имя файла по относительному, относительно физического
     * расположения тестового класса, откуда был вызван.
     */
    public String getTestFile(String filename) {
        filename = replaceTestName(filename);
        if (UtFile.isAbsolute(filename)) {
            return filename;
        }
        URL u = getTest().getClass().getResource(filename);
        if (u == null) {
            throw new RuntimeException(String.format("Test file [%s] not found for class [%s]", //NON-NLS
                    filename, getTest().getClass().getName()));
        }
        try {
            return URLDecoder.decode(u.getFile(), UtString.UTF8);
        } catch (UnsupportedEncodingException e) {
            return u.getFile();
        }
    }

    ////// logging

    /**
     * Отключение логирования
     */
    public void logOff() throws Exception {
        UtLog.logOff();
    }

    /**
     * Включение логирования для выполняемого метода.
     */
    public void logOn() throws Exception {
        UtLog.logOn();
    }

    /**
     * Включение логирования для выполняемого метода с конфигураций.
     *
     * @param cfg либо имя локального файла, либо строка с xml-конфигурацией для logback
     */
    public void logOn(String cfg) throws Exception {
        UtLog.logOn(cfg);
    }

    /**
     * Вывод лога от имени текущего тестового класса
     */
    public void log(Object message) {
        Logger lg = LoggerFactory.getLogger(getTest().getClass());
        lg.info(UtString.toString(message));
    }

    ////// outs

    /**
     * Вывод разделителя
     *
     * @param delimChar символ разделителя
     * @param msg       сообщение в разделителе
     */
    public void delim(String msg, String delimChar) {
        System.out.println(UtString.delim(msg, delimChar, 76));
    }

    /**
     * Вывод разделителя
     *
     * @param msg сообщение в разделителе
     */
    public void delim(String msg) {
        delim(msg, "~");
    }

    /**
     * Вывод разделителя
     */
    public void delim() {
        delim("");
    }

    /**
     * Вывести map на консоль в удобочитаемом виде
     */
    public void outMap(Map m, boolean showClass) {
        OutMapSaver sv = new OutMapSaver(m, showClass);
        String s = sv.save().toString();
        System.out.println(s);
    }

    /**
     * Вывести map на консоль в удобочитаемом виде
     */
    public void outMap(Map m) {
        outMap(m, false);
    }

    /**
     * Вывести таблицу на консоль
     *
     * @param data  данные
     * @param limit сколько записей выводить
     */
    public void outTable(Object data, int limit) {
        OutTableSaver tb = UtOutTable.createOutTableSaver(data);
        tb.setLimit(limit);
        System.out.println(tb.save().toString());
    }

    /**
     * Вывести таблицу на консоль
     *
     * @param data данные
     */
    public void outTable(Object data) {
        outTable(data, -1);
    }

    ////// errors

    /**
     * Показать ошибку в удобном виде
     */
    public void showError(Throwable e) {
        ErrorInfo ei = UtError.createErrorInfo(e);
        delim("ERROR", "="); //NON-NLS
        System.out.println(ei.getText());
        String s = ei.getTextErrorSource();
        if (!UtString.empty(s)) {
            delim("source", "-"); //NON-NLS
            System.out.println(s);
        }
        delim("filtered stack", "-"); //NON-NLS
        System.out.println(ei.getTextStack(true));
        delim("", "=");
    }

    ////// shell

    /**
     * При значении true - тесты выполняются из командной строки либо
     * выполняется не первый тест. Для организации интерактивности в отдельных
     * тестах
     */
    public boolean isBatchMode() {
        String a = System.getProperty("jandcode.batchtest");
        if ("true".equals(a)) { //NON-NLS
            return true;  // в батнике
        }
        if (Base_Test.testNum > 1) {
            return true;  // не первый тест
        }
        return false;
    }

    /**
     * Открыть указанный файл в shell (например .xls или .html).
     * Для организации комфортного просмотра результата генерации файлов.
     * Работает только в режиме запуска одного теста. В случае выполнения
     * в командной строке либо не первого теста - вызов игнорируется.
     *
     * @param filename
     */
    public void shellOpen(String filename) throws Exception {
        if (isBatchMode()) {
            return; // работает только в режиме запуска одного теста
        }
        filename = UtFile.abs(filename);
        boolean osWindows = (File.separatorChar == '\\');
        if (osWindows) {
            filename = filename.replace("/", "\\");
            Runtime.getRuntime().exec("cmd.exe /c start " + filename); //NON-NLS
        }
        // ignore
    }

}
