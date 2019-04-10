package jandcode.commons.error.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;

import java.util.*;

/**
 * Информация об ошибке, преобразованная к удобному виду
 */
public class ErrorInfoImpl implements ErrorInfo {

    private List<ErrorItem> _errors = new ArrayList<ErrorItem>();
    private List<Throwable> _exceptions = new ArrayList<Throwable>();
    private Throwable _exception;

    /**
     * Элемент списка ошибок
     */
    public class ErrorItem {

        private Throwable _exception;
        private List<Throwable> _marks = new ArrayList<Throwable>();

        public Throwable getException() {
            return _exception;
        }

        public void setException(Throwable exception) {
            _exception = exception;
        }

        public List<Throwable> getMarks() {
            return _marks;
        }

    }

    //////

    public Throwable getException() {
        return _exception;
    }

    public void setException(Throwable exception) {
        _exception = exception;
        _errors.clear();
        // раскрываем в plain-список
        _exceptions.clear();
        expandException(_exception, _exceptions);
        // формируем список errors
        ErrorItem it = new ErrorItem();
        for (Throwable e : _exceptions) {
            if (e instanceof IErrorMark) {
                it.getMarks().add(e);
            } else {
                it.setException(e);
                _errors.add(it);
                it = new ErrorItem();
            }
        }
        // последний тоже возможно нужно занести
        if (it.getMarks().size() > 0 && it.getException() == null) {
            // маркер есть, но ошибки нет. имитируем
            it.setException(new Exception("Unknown marked error"));
            _errors.add(it);
        }
        // все...
    }

    /**
     * Линейный список exception, сформированный по корневому и включающий в себя
     * все нужные в стеке exception. В этом списке уже отфильтрованы все известные
     * обертки и вспомогательные exception.
     */
    public List<Throwable> getExceptions() {
        return _exceptions;
    }

    /**
     * Список ошибок, построенный на основе getExceptions(). Каждая ошибка представлена
     * exception и набором exception-markers.
     */
    public List<ErrorItem> getErrors() {
        return _errors;
    }

    //////

    /**
     * Раскрытие e в список реальных ошибок
     *
     * @param e   с чего начинать
     * @param lst куда помещать
     */
    protected void expandException(Throwable e, List<Throwable> lst) {
        if (e == null || lst.contains(e)) {
            return;
        }
        e = UtError.getErrorConvertor().getReal(e);
        Throwable nextE = UtError.getErrorConvertor().getNext(e);

        lst.add(e);
        expandException(nextE, lst);
    }

    /**
     * Возвращает список источников ошибок в исходниках скриптов
     */
    public List<ErrorSource> getErrorSources() {
        List<ErrorSource> res = new ArrayList<ErrorSource>();
        for (Throwable e : getExceptions()) {
            List<ErrorSource> s = UtError.getErrorConvertor().getErrorSource(e);
            if (s != null) {
                res.addAll(s);
            }
        }
        return res;
    }

    //////

    /**
     * Тект ошибки в виде строки
     */
    public String getText() {
        Set<String> marksText = new HashSet<String>();

        StringBuilder sb = new StringBuilder();
        for (ErrorItem e : getErrors()) {
            // сначала сама ошибка
            String s = UtError.getErrorConvertor().getText(e.getException());
            if (!marksText.contains(s)) {
                marksText.add(s);
                sb.append(s).append("\n");
            }

            // потом - маркеры
            Set<String> marksUni = new HashSet<String>();
            for (Throwable em : e.getMarks()) {
                s = UtError.getErrorConvertor().getText(em);
                if (marksUni.contains(s)) {
                    continue;
                }
                marksUni.add(s);
                sb.append("    ").append(s).append("\n");
            }

        }
        String s = sb.toString().trim();
        if (s.length() == 0) {
            s = getException().toString().trim();
        }
        return s;
    }

    protected void appendExcStackText(StringBuilder sb, Throwable e, boolean filtered) {
        sb.append(e.getClass().getName() + ": " + UtError.getErrorConvertor().getText(e)).append("\n");
        for (StackTraceElement st : e.getStackTrace()) {
            if (filtered && UtError.getErrorConvertor().isFiltered(st.getClassName())) {
                continue;
            }
            sb.append("\tat ").append(st).append("\n"); //NON-NLS
        }
    }

    /**
     * Получить текст стека
     *
     * @param filtered true - отфильтрованный
     */
    public String getTextStack(boolean filtered) {
        StringBuilder sb = new StringBuilder();
        if (filtered) {
            for (ErrorItem item : getErrors()) {
                appendExcStackText(sb, item.getException(), filtered);
            }
        } else {
            for (Throwable e : getExceptions()) {
                appendExcStackText(sb, e, filtered);
            }
        }
        return sb.toString().trim();
    }

    /**
     * Получить текст для идентификации исходников, где возникла ошибка
     */
    public String getTextErrorSource() {
        List<ErrorSource> es = getErrorSources();
        if (es.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (ErrorSource e : es) {
            if (e.getLineNum() == -1) {
                continue;
            }

            String fn = e.getSourceName();
            String sn = null;
            if (fn != null) {
                sn = UtFile.filename(fn);
            }

            if (sn != null) {
                sb.append(sn);
            } else if (fn != null) {
                sb.append(fn);
            } else {
                sb.append("UNKNOWN"); //NON-NLS
            }

            sb.append(":").append(e.getLineNum());

            if (fn != null && !fn.equals(sn)) {
                sb.append(" (").append(fn).append(")");
            }

            sb.append("\n");

            if (e.getLineText() != null) {
                sb.append("    source: ").append(e.getLineText()).append("\n"); //NON-NLS
            }

            if (e.getLineTextPrepared() != null) {
                String ps = e.getLineTextPrepared();
                String ts = e.getLineText();
                if (ts == null) {
                    ts = "";
                }
                ps = ps.trim();
                ts = ts.trim();
                if (!ps.equals(ts)) {
                    sb.append("  prepared: ").append(UtString.trimLast(e.getLineTextPrepared())).append("\n"); //NON-NLS
                }
            }

        }
        return sb.toString().trim();
    }

}
