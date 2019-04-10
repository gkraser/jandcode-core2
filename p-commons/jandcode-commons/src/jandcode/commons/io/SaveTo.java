package jandcode.commons.io;

import jandcode.commons.*;

import java.io.*;

public class SaveTo {

    private ISaver saver;

    public SaveTo(ISaver saver) {
        this.saver = saver;
    }

    /**
     * Записать в массив байт в кодировке utf-8
     */
    public byte[] toBytes() throws Exception {
        return UtSave.toBytes(saver);
    }

    /**
     * Записать в массив байт в указанной кодировке
     */
    public byte[] toBytes(String charset) throws Exception {
        return UtSave.toBytes(saver, charset);
    }

    /**
     * Записать в файл в кодировке utf-8
     */
    public void toFile(String filename) throws Exception {
        UtSave.toFile(saver, filename);
    }

    /**
     * Записать в файл в указанной кодировке
     */
    public void toFile(String filename, String charset) throws Exception {
        UtSave.toFile(saver, filename, charset);
    }

    /**
     * Записать в файл в кодировке utf-8
     */
    public void toFile(File file) throws Exception {
        UtSave.toFile(saver, file);
    }

    /**
     * Записать в файл в указанной кодировке
     */
    public void toFile(File file, String charset) throws Exception {
        UtSave.toFile(saver, file, charset);
    }

    /**
     * Записать во Writer. После записи writer закрывается.
     */
    public void toWriter(Writer writer) throws Exception {
        UtSave.toWriter(saver, writer);
    }

    /**
     * Записать во OutputStream в кодировке urf-8. После записи stream закрывается.
     */
    public void toStream(OutputStream stream) throws Exception {
        UtSave.toStream(saver, stream);
    }

    /**
     * Записать во OutputStream в указанной кодировке. После записи stream закрывается.
     */
    public void toStream(OutputStream stream, String charset) throws Exception {
        UtSave.toStream(saver, stream, charset);
    }

    /**
     * Записать во строку в указанной кодировке.
     */
    public String toString(String charset) {
        return UtSave.toString(saver, charset);
    }

    /**
     * Записать во строку в кодировке urf-8.
     */
    public String toString() {
        return UtSave.toString(saver);
    }

}
