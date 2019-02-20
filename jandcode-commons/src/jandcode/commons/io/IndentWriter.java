package jandcode.commons.io;

import jandcode.commons.impl.*;

import java.io.*;

/**
 * Вывод во Writer с автоотступами
 */
public class IndentWriter extends Writer {

    private Writer _writer;
    private ChrBuilder _indentString = new ChrBuilder();
    private boolean _needIndent = true;
    private int _indentDelta = 4;
    private char _indentChar = ' ';
    private int _col = 0;
    private int _row = 0;
    private boolean _enable = true;

    public IndentWriter(Writer writer) {
        _writer = writer;
    }

    //

    public void write(char cbuf[], int off, int len) throws IOException {
        if (isEnable()) {
            int fromI = off;
            int cnt = 0;

            for (int i = off; i < off + len; i++) {
                if (_needIndent) {
                    _needIndent = false;

                    _writer.write(cbuf, fromI, cnt);
                    cnt = 0;
                    fromI = i;
                    _writer.write(_indentString.getData(), 0, _indentString
                            .getLength());

                    _col = _col + _indentString.getLength();
                }
                char c = cbuf[i];
                _col++;
                cnt++;
                if (c == '\n') {
                    _needIndent = true;
                    _col = 0;
                    _row++;
                }
            }
            if (fromI < off + len) {
                _writer.write(cbuf, fromI, off + len - fromI);
            }
        } else {
            _writer.write(cbuf, off, len);
            for (int i = off + len - 1; i >= off; i--) {
                if (cbuf[i] == '\n') {
                    _col = off + len - i;
                    break;
                }
            }
            _col = _col + len;
        }
    }

    public void flush() throws IOException {
        _writer.flush();
    }

    public void close() throws IOException {
        _writer.flush();
        _writer.close();
    }

    /**
     * Получить приращение выравнивания для метода incIndent() и decIndent() без
     * параметров. По умолчанию - 4.
     */
    public int getIndentDelta() {
        return _indentDelta;
    }

    /**
     * Установить приращение выравнивания для метода incIndent() и decIndent()
     * без параметров
     */
    public void setIndentDelta(int indentDelta) {
        _indentDelta = indentDelta;
    }

    /**
     * Символ, используемый для выравнивания. По умолчанию - пробел
     */
    public char getIndentChar() {
        return _indentChar;
    }

    /**
     * Символ, используемый для выравнивания
     */
    public void setIndentChar(char indentChar) {
        _indentChar = indentChar;
    }

    /**
     * Увеличить выравнивание на count символов getIndentChar
     */
    public void indentInc(int count) {
        for (int i = 0; i < count; i++) {
            _indentString.append(_indentChar);
        }
    }

    /**
     * Увеличить выравнивание на символы строки
     */
    public void indentInc(String str) {
        _indentString.append(str);
    }

    /**
     * Уменьшить выравнивание на количество символов строки
     */
    public void indentDec(String str) {
        indentDec(str.length());
    }

    /**
     * Уменьшить выравнивание на count символов
     */
    public void indentDec(int count) {
        if (count > _indentString.getLength()) {
            _indentString.setLength(0);
        } else {
            _indentString.setLength(_indentString.getLength() - count);
        }
    }

    /**
     * Увеличить выравнивание на getIndentDelta символов getIndentChar
     */
    public void indentInc() {
        indentInc(getIndentDelta());
    }

    /**
     * Уменьшить выравнивание на getIndentDelta символов
     */
    public void indentDec() {
        indentDec(getIndentDelta());
    }

    /**
     * Текущая колонка
     */
    public int getCol() {
        return _col;
    }

    /**
     * Текущая строка
     */
    public int getRow() {
        return _row;
    }

    /**
     * Проверить выключеность/выключенность обработки выравнивания
     */
    public boolean isEnable() {
        return _enable;
    }

    /**
     * Выключить/включить обработку выравнивания
     */
    public void setEnable(boolean enable) {
        _enable = enable;
    }

}
