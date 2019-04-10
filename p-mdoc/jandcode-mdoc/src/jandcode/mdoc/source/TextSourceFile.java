package jandcode.mdoc.source;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.mdoc.source.impl.*;

import java.io.*;

/**
 * Исходный файл, созданный на лету по тексту
 */
public class TextSourceFile extends BaseSourceFile {

    private String text;
    private ITextGenerator textGenerator;
    private long lastModTime;

    /**
     * @param path виртуальный путь файла
     * @param text текст файла
     */
    public TextSourceFile(String path, String text) {
        setPath(path);
        setText(text);
        this.lastModTime = System.currentTimeMillis();
    }

    /**
     * @param path          виртуальный путь файла
     * @param textGenerator генератор текста
     */
    public TextSourceFile(String path, ITextGenerator textGenerator) {
        setPath(path);
        this.textGenerator = textGenerator;
    }

    public void copyTo(String destFile) throws Exception {
        UtFile.saveString(getText(), new File(destFile));
    }

    public void copyTo(OutputStream stm) throws Exception {
        String txt = getText();
        stm.write(txt.getBytes());
        stm.flush();
    }

    public String getText() {
        if (this.textGenerator != null) {
            try {
                return this.textGenerator.generateText();
            } catch (Exception e) {
                throw new XErrorMark(e, "файл: " + getPath());
            }
        }
        if (this.text == null) {
            return "";
        }
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getLastModTime() {
        return lastModTime;
    }
}
