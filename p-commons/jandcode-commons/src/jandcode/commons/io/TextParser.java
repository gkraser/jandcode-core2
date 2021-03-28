package jandcode.commons.io;

import jandcode.commons.*;
import jandcode.commons.error.*;

import java.io.*;

/**
 * Абстрактный парзер текста
 */
public abstract class TextParser implements ILoader {

    public static char EOF = (char) -1;

    /**
     * Последний полученный методом next() символ
     */
    public char last;

    //////

    private char[] _push = new char[10];
    private int _pushPos;
    private int _lastcol;
    private int _col;
    private int _row;
    private int _pos;
    private Reader _reader;

    class FastCharReader extends Reader {

        private CharSequence source;
        private int sourceLen;
        private int sourceIdx;

        FastCharReader(CharSequence source) {
            if (source == null) {
                this.source = "";
            } else {
                this.source = source;
            }
            this.sourceIdx = -1;
            this.sourceLen = source.length();
        }

        public int read() throws IOException {
            sourceIdx++;
            if (sourceIdx >= sourceLen) {
                return EOF;
            } else {
                return source.charAt(sourceIdx);
            }
        }

        public int read(char[] cbuf, int off, int len) throws IOException {
            return EOF;
        }

        public void close() throws IOException {
        }
    }

    /**
     * Ошибка разбора
     */
    public class ErrorParse extends RuntimeException implements IErrorMark {
        private int row;
        private int col;

        public ErrorParse(Throwable cause) {
            super(cause);
            this.row = getRow();
            this.col = getCol();
        }

        public String getMessage() {
            return "row:" + row + ", col:" + col;
        }
    }

    public void loadFrom(Reader reader) throws Exception {
        _reader = reader;
        last = EOF;
        _pushPos = -1;
        _col = 0;
        _row = 1;
        _pos = -1;
        _lastcol = 0;
        try {
            next();
            push(last);
            onParse();
        } catch (Exception e) {
            throw new ErrorParse(e);
        }
    }

    /**
     * Загрузить из строки. Работает быстрее чем loadFrom(Reader)
     *
     * @param source откуда
     */
    public void loadFrom(CharSequence source) {
        try {
            loadFrom(new FastCharReader(source));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Перекрыть для разбора
     */
    protected abstract void onParse() throws Exception;

    //////

    /**
     * Получить следующий символ из потока. Если символов в потоке больше нет,
     * то возвращается -1
     */
    public char next() throws Exception {
        if (_pushPos >= 0) {
            last = _push[_pushPos];
            _pushPos--;
        } else {
            last = (char) _reader.read();
            if (last != EOF) {
                _pos++;
                if (last == '\n') {
                    _lastcol = _col + 1;
                    _col = 0;
                    _row++;
                } else {
                    _col++;
                }
            }
        }
        return last;
    }

    /**
     * Возвращает символ обратно в поток. Может вызыватся для любых символов.
     */
    public void push(char c) {
        _pushPos++;
        _push[_pushPos] = c;
    }

    //////

    /**
     * Генерация ошибки с номером строки и колонки
     */
    public void error(String s) throws Exception {
        throw new Exception(s);
    }

    /**
     * Генерация ошибки с номером строки и колонки
     */
    public void errorWaitChars(String chars) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chars.length(); i++) {
            if (sb.length() != 0) {
                sb.append(',');
            }
            sb.append("\"");
            sb.append(chars.charAt(i));
            sb.append("\"");
        }
        error(UtLang.t("Ожидается символ: {0}", sb.toString()));
    }

    /**
     * Генерация ошибки с номером строки и колонки
     */
    public void errorWaitString(String str) throws Exception {
        error(UtLang.t("Ожидается комбинация символов: {0}", str));
    }

    //////

    /**
     * Текущая позиция в потоке
     */
    public int getPos() {
        return _pos;
    }

    /**
     * Текущая строка в потоке
     */
    public int getRow() {
        if (_col == 0) {
            return _row - 1;
        } else {
            return _row;
        }
    }

    /**
     * Текущая колонка в потоке
     */
    public int getCol() {
        if (_col == 0) {
            return _lastcol;
        } else {
            return _col;
        }
    }

}
