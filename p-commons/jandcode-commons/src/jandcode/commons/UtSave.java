package jandcode.commons;

import jandcode.commons.error.*;
import jandcode.commons.io.*;
import jandcode.commons.io.impl.*;

import java.io.*;

/**
 * Утилиты для записи во Writer.
 * Запись производится в объект, реализующий интерфейс {@link ISaver}.
 */
public class UtSave {

    //////

    /**
     * Запись в writer. Все вызовы методов так или иниче приводят сюда
     */
    protected static void saveWriter(ISaver w, Writer writer, String filename, String charset) throws Exception {
        if (!(writer instanceof BufferedWriter)) {
            writer = new BufferedWriter(writer);
        }
        if (!(writer instanceof IWriterExt)) {
            writer = new WriterExtImpl(writer, filename, charset);
        }
        try {
            w.saveTo(writer);
            writer.flush();
        } catch (Exception e) {
            if (!UtString.empty(filename)) {
                throw new XErrorMark(e, filename);
            } else {
                throw e;
            }
        }
    }

    /**
     * Запись потока. Все вызовы, которые приводят к записи потока приводят сюда.
     */
    protected static void saveStream(ISaver w, OutputStream strm, String filename, String charset) throws Exception {
        saveWriter(w, new OutputStreamWriter(strm, charset), filename, charset);
    }

    //////

    /**
     * Записать в массив байт в кодировке utf-8
     */
    public static byte[] toBytes(ISaver w) throws Exception {
        return toBytes(w, UtString.UTF8);
    }

    /**
     * Записать в массив байт в указанной кодировке
     */
    public static byte[] toBytes(ISaver w, String charset) throws Exception {
        ByteArrayOutputStream strm = new ByteArrayOutputStream();
        byte[] res = null;
        try {
            saveStream(w, strm, null, charset);
            res = strm.toByteArray();
        } finally {
            strm.close();
        }
        return res;
    }

    /**
     * Записать в файл в кодировке utf-8
     */
    public static void toFile(ISaver w, String filename) throws Exception {
        toFile(w, new File(filename), UtString.UTF8);
    }

    /**
     * Записать в файл в указанной кодировке
     */
    public static void toFile(ISaver w, String filename, String charset) throws Exception {
        toFile(w, new File(filename), charset);
    }

    /**
     * Записать в файл в кодировке utf-8
     */
    public static void toFile(ISaver w, File file) throws Exception {
        toFile(w, file, UtString.UTF8);
    }

    /**
     * Записать в файл в указанной кодировке
     */
    public static void toFile(ISaver w, File file, String charset) throws Exception {
        FileOutputStream strm = new FileOutputStream(file);
        try {
            saveStream(w, strm, file.getAbsolutePath(), charset);
        } finally {
            strm.close();
        }
    }

    /**
     * Записать во Writer. После записи writer закрывается.
     */
    public static void toWriter(ISaver w, Writer writer) throws Exception {
        try {
            saveWriter(w, writer, null, UtString.UTF8);
        } finally {
            writer.close();
        }
    }

    /**
     * Записать во OutputStream в кодировке urf-8. После записи stream закрывается.
     */
    public static void toStream(ISaver w, OutputStream stream) throws Exception {
        toStream(w, stream, UtString.UTF8);
    }

    /**
     * Записать во OutputStream в указанной кодировке. После записи stream закрывается.
     */
    public static void toStream(ISaver w, OutputStream stream, String charset) throws Exception {
        try {
            saveStream(w, stream, null, charset);
        } catch (Exception e) {
            stream.close();
        }
    }

    /**
     * Записать во строку в указанной кодировке.
     */
    public static String toString(ISaver w, String charset) {
        try {
            String s = "";
            StringWriter f = new StringWriter();
            try {
                saveWriter(w, f, null, charset);
                s = f.toString();
            } finally {
                f.close();
            }
            return s;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Записать во строку в кодировке urf-8.
     */
    public static String toString(ISaver w) {
        return toString(w, UtString.UTF8);
    }

    //////

    /**
     * Возвращает имя файла для writer
     */
    public static String getFilename(Writer writer) {
        if (writer instanceof IWriterExt) {
            return ((IWriterExt) writer).getFilename();
        }
        return "";
    }

    /**
     * Возвращает charset для writer
     */
    public static String getCharset(Writer writer) {
        if (writer instanceof IWriterExt) {
            return ((IWriterExt) writer).getCharset();
        }
        return UtString.UTF8;
    }


}
