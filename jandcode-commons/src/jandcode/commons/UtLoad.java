package jandcode.commons;

import jandcode.commons.error.*;
import jandcode.commons.io.*;
import jandcode.commons.io.impl.*;
import org.apache.commons.vfs2.*;
import org.xml.sax.*;

import java.io.*;
import java.net.*;

/**
 * Утилиты для чтения из Reader.
 * Чтение производится из объекта, реализующиго интерфейс {@link ILoader}.
 */
public class UtLoad {

    /**
     * Загрузка из reader. Все вызовы методов так или иначе приводят сюда
     */
    protected static void loadReader(ILoader r, Reader reader, String filename, String charset, InputStream stream) throws Exception {
        if (!(reader instanceof BufferedReader)) {
            reader = new BufferedReader(reader);
        }
        if (!(reader instanceof IReaderExt)) {
            reader = new ReaderExtImpl(reader, filename, charset, stream);
        }

        try {
            try {
                r.loadFrom(reader);
            } finally {
                try {
                    reader.close();
                } catch (Exception e) {
                    //
                }
            }
        } catch (Exception e) {
            if (!UtString.empty(filename)) {
                throw new XErrorMark(e, filename);
            } else {
                throw e;
            }
        }
    }

    /**
     * Загрузка потока. Все вызовы, которые приводят к загрузке потока приводят сюда.
     */
    protected static void loadStream(ILoader r, InputStream stream, String filename, String charset) throws Exception {
        loadReader(r, new InputStreamReader(stream, charset), filename, charset, stream);
    }

    //////

    /**
     * Загрузить из массива байт в кодировке utf-8
     */
    public static void fromBytes(ILoader r, byte[] bytes) throws Exception {
        fromBytes(r, bytes, UtString.UTF8);
    }

    /**
     * Загрузить из массива байт в указанной кодировке
     */
    public static void fromBytes(ILoader r, byte[] bytes, String charset) throws Exception {
        loadStream(r, new ByteArrayInputStream(bytes), null, charset);
    }

    /**
     * Загрузить из файла в кодировке utf-8
     */
    public static void fromFile(ILoader r, String filename) throws Exception {
        fromFile(r, new File(filename), UtString.UTF8);
    }

    /**
     * Загрузить из файла в указанной кодировке
     */
    public static void fromFile(ILoader r, String filename, String charset) throws Exception {
        fromFile(r, new File(filename), charset);
    }

    /**
     * Загрузить из файла в кодировке utf-8
     */
    public static void fromFile(ILoader r, File file) throws Exception {
        fromFile(r, file, UtString.UTF8);
    }

    /**
     * Загрузить из файла в указанной кодировке
     */
    public static void fromFile(ILoader r, File file, String charset) throws Exception {
        loadStream(r, new FileInputStream(file), file.getAbsolutePath(), charset);
    }

    /**
     * Загрузить из Reader. После загрузки reader закрывается.
     */
    public static void fromReader(ILoader r, Reader reader) throws Exception {
        loadReader(r, reader, null, UtString.UTF8, null);
    }

    /**
     * Загрузить из InputStream в кодировке utf-8. После загрузки stream закрывается.
     */
    public static void fromStream(ILoader r, InputStream stream) throws Exception {
        fromStream(r, stream, UtString.UTF8);
    }

    /**
     * Загрузить из InputStream в указанной кодировке. После загрузки stream закрывается.
     */
    public static void fromStream(ILoader r, InputStream stream, String charset) throws Exception {
        loadStream(r, stream, null, charset);
    }

    /**
     * Загрузить из строки
     */
    public static void fromString(ILoader r, String data) throws Exception {
        if (data == null) {
            data = "";
        }
        loadReader(r, new StringReader(data), null, UtString.UTF8, null);
    }

    /**
     * Загрузить из строки
     */
    public static void fromString(ILoader r, String data, String dummyFilename) throws Exception {
        if (data == null) {
            data = "";
        }
        loadReader(r, new StringReader(data), dummyFilename, UtString.UTF8, null);
    }

    /**
     * Загрузить из url (как его понимает java) в кодировке utf-8
     */
    public static void fromURL(ILoader r, URL url) throws Exception {
        fromURL(r, url, UtString.UTF8);
    }

    /**
     * Загрузить из url (как его понимает java) в указанной кодировке
     */
    public static void fromURL(ILoader r, URL url, String charset) throws Exception {
        loadStream(r, url.openStream(), url.toString(), charset);
    }

    /**
     * Загрузить из файла VFS в кодировке utf-8
     */
    public static void fromFileObject(ILoader r, String filename) throws Exception {
        fromFileObject(r, UtFile.getFileObject(filename), UtString.UTF8);
    }

    /**
     * Загрузить из файла VFS в указанной кодировке
     */
    public static void fromFileObject(ILoader r, String filename, String charset) throws Exception {
        fromFileObject(r, UtFile.getFileObject(filename), charset);
    }

    /**
     * Загрузить из файла VFS в кодировке utf-8
     */
    public static void fromFileObject(ILoader r, FileObject file) throws Exception {
        fromFileObject(r, file, UtString.UTF8);
    }

    /**
     * Загрузить из файла VFS в указанной кодировке
     */
    public static void fromFileObject(ILoader r, FileObject file, String charset) throws Exception {
        InputStream strm = file.getContent().getInputStream();
        loadStream(r, strm, file.toString(), charset);
    }

    /**
     * Загрузить из ресурса vfs. Если путь начинается с '/', то считается ресурсом
     */
    public static void fromRes(ILoader r, String path, String charset) throws Exception {
        fromFileObject(r, UtCnv.toVfsPath(path), charset);
    }

    /**
     * Загрузить из ресурса vfs. Если путь начинается с '/', то считается ресурсом
     */
    public static void fromRes(ILoader r, String path) throws Exception {
        fromRes(r, UtCnv.toVfsPath(path), UtString.UTF8);
    }

    //////

    /**
     * Возвращает InputSource для reader
     */
    public static InputSource getInputSource(Reader reader) {
        if (reader instanceof IReaderExt) {
            return ((IReaderExt) reader).getInputSource();
        }
        return new InputSource(reader);
    }

    /**
     * Возвращает имя файла для reader
     */
    public static String getFilename(Reader reader) {
        if (reader instanceof IReaderExt) {
            return ((IReaderExt) reader).getFilename();
        }
        return "";
    }

    /**
     * Возвращает charset для reader
     */
    public static String getCharset(Reader reader) {
        if (reader instanceof IReaderExt) {
            return ((IReaderExt) reader).getCharset();
        }
        return UtString.UTF8;
    }

}
