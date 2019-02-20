package jandcode.commons.io;

/**
 * Построчный текстовый парсер.
 * Перекрывать нужно onParse. Следующую строку получать через nextStr,
 * а затем через getStr, getStrFull и getSpaces получать данные строки.
 */
public abstract class TextParserStr extends TextParser {

    /**
     * Текущая обрабатываемая строка. Без начальных пробелов
     */
    private StringBuilder _curr = new StringBuilder();

    /**
     * _curr представленная в виде строки
     */
    private String _str;

    /**
     * Начальные пробелы в текущей строке
     */
    private StringBuilder _spaces = new StringBuilder();
    private String _spacesStr;

    /**
     * строка с пробелами
     */
    private String _strFull;

    /**
     * Получение следующей строки
     */
    public boolean nextStr() throws Exception {
        _str = null;
        _spacesStr = null;
        _strFull = null;
        _curr.setLength(0);
        _spaces.setLength(0);
        while (true) {
            char c = next();
            if (c == EOF) {
                break;
            }
            if (c == '\r') {
                continue;
            } else if (c == '\n') { // пустые строки в начале пропускаем
                break;
            } else if (c == ' ') {
                if (_curr.length() == 0) {
                    _spaces.append(c);
                } else {
                    _curr.append(c);
                }
            } else {
                _curr.append(c);
            }
        }
        // удаляем пробелы в конце
        int i = _curr.length() - 1;
        while (i >= 0) {
            char c = _curr.charAt(i);
            if (c == ' ') {
                _curr.setLength(i);
            } else {
                break;
            }
            i--;
        }
        return last != EOF;
    }

    /**
     * Возвращает последнюю строку без пробелов в начале и в конце
     */
    protected String getStr() {
        if (_str == null) {
            _str = _curr.toString().trim();
        }
        return _str;
    }

    protected void setStr(String str) {
        _str = str;
    }

    /**
     * Возвращает последнюю строку пробелов в начале строки
     */
    protected String getSpaces() {
        if (_spacesStr == null) {
            _spacesStr = _spaces.toString();
        }
        return _spacesStr;
    }

    /**
     * Возвращает последнюю полную строку с пробелами в начале
     */
    protected String getStrFull() {
        if (_strFull == null) {
            _strFull = getSpaces() + getStr();
        }
        return _strFull;
    }

}
