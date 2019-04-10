package jandcode.web.gsp.impl;

import jandcode.commons.*;
import jandcode.web.gsp.*;
import org.codehaus.groovy.runtime.*;

import java.io.*;
import java.util.*;

/**
 * Буфер вывода текста для рендеринга.
 * Объекты null заменяются при выводе на пустую строку.
 */
public class TextBufferImpl implements CharSequence, ITextBuffer {

    protected StringBuilder sb = new StringBuilder();
    private Object owner;

    public TextBufferImpl() {
    }

    public TextBufferImpl(Object owner) {
        this.owner = owner;
    }

    /**
     * Вернуть текст буфера в виде строки
     */
    public String toString() {
        return sb.toString();
    }

    /**
     * Вывести значение в буфер. Основной метода для использования.
     *
     * @param s что вывести
     * @return самого себя
     */
    public TextBufferImpl out(Object s) {
        if (s != null) {
            if (s instanceof TextBufferImpl) {
                append((TextBufferImpl) s);
            } else if (s instanceof CharSequence) {
                append(s.toString());
            } else if (s instanceof Map) {
                append(DefaultGroovyMethods.toMapString((Map) s));
            } else if (s instanceof Collection) {
                append(DefaultGroovyMethods.toListString((Collection) s));
            } else {
                append(s.toString());
            }
        }
        return this;
    }

    /**
     * Добавить текст из другого TextBufferImpl.
     * Подразумевается что у b такая же реализация, как и у this.
     *
     * @param b откуда добавлять
     */
    protected void append(TextBufferImpl b) {
        sb.append(b.sb);
    }

    /**
     * Добавить строку.
     *
     * @param b откуда добавлять
     */
    protected void append(String b) {
        sb.append(b);
    }

    /**
     * Проверка на пустой буфер
     */
    public boolean isEmpty() {
        return sb.length() == 0;
    }

    /**
     * Проверка, что буфер состоит только из пробельных символов (включая переводы строк
     * и табуляцию).
     */
    public boolean isWhite() {
        return UtString.isWhite(sb);
    }

    /**
     * Записать буфер во writer
     */
    public void writeTo(Writer writer) throws IOException {
        writer.write(sb.toString());
    }

    ////// char sequence

    public int length() {
        return sb.length();
    }

    public char charAt(int index) {
        return sb.charAt(index);
    }

    public CharSequence subSequence(int start, int end) {
        return sb.subSequence(start, end);
    }

    //////

    /**
     * Владелец буфера
     */
    public Object getOwner() {
        return owner;
    }

}
