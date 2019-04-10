package jandcode.commons.io;

import jandcode.commons.*;
import org.apache.commons.vfs2.*;

import java.io.*;
import java.net.*;

public class LoadFrom {

    private ILoader loader;

    public LoadFrom(ILoader loader) {
        this.loader = loader;
    }

    /**
     * Загрузить из массива байт в кодировке utf-8
     */
    public void fromBytes(byte[] bytes) throws Exception {
        UtLoad.fromBytes(loader, bytes);
    }

    /**
     * Загрузить из массива байт в указанной кодировке
     */
    public void fromBytes(byte[] bytes, String charset) throws Exception {
        UtLoad.fromBytes(loader, bytes, charset);
    }

    /**
     * Загрузить из файла в кодировке utf-8
     */
    public void fromFile(String filename) throws Exception {
        UtLoad.fromFile(loader, filename);
    }

    /**
     * Загрузить из файла в указанной кодировке
     */
    public void fromFile(String filename, String charset) throws Exception {
        UtLoad.fromFile(loader, filename, charset);
    }

    /**
     * Загрузить из файла в кодировке utf-8
     */
    public void fromFile(File file) throws Exception {
        UtLoad.fromFile(loader, file);
    }

    /**
     * Загрузить из файла в указанной кодировке
     */
    public void fromFile(File file, String charset) throws Exception {
        UtLoad.fromFile(loader, file, charset);
    }

    /**
     * Загрузить из Reader. После загрузки reader закрывается.
     */
    public void fromReader(Reader reader) throws Exception {
        UtLoad.fromReader(loader, reader);
    }

    /**
     * Загрузить из InputStream в кодировке utf-8. После загрузки stream закрывается.
     */
    public void fromStream(InputStream stream) throws Exception {
        UtLoad.fromStream(loader, stream);
    }

    /**
     * Загрузить из InputStream в указанной кодировке. После загрузки stream закрывается.
     */
    public void fromStream(InputStream stream, String charset) throws Exception {
        UtLoad.fromStream(loader, stream, charset);
    }

    /**
     * Загрузить из строки
     */
    public void fromString(String data) throws Exception {
        UtLoad.fromString(loader, data);
    }

    /**
     * Загрузить из строки
     *
     * @param dummyFilename имя файла, откуда как бы загружаем
     */
    public void fromString(String data, String dummyFilename) throws Exception {
        UtLoad.fromString(loader, data, dummyFilename);
    }

    /**
     * Загрузить из url (как его понимает java) в кодировке utf-8
     */
    public void fromURL(URL url) throws Exception {
        UtLoad.fromURL(loader, url);
    }

    /**
     * Загрузить из url (как его понимает java) в указанной кодировке
     */
    public void fromURL(URL url, String charset) throws Exception {
        UtLoad.fromURL(loader, url, charset);
    }

    /**
     * Загрузить из файла VFS в кодировке utf-8
     */
    public void fromFileObject(String filename) throws Exception {
        UtLoad.fromFileObject(loader, filename);

    }

    /**
     * Загрузить из файла VFS в указанной кодировке
     */
    public void fromFileObject(String filename, String charset) throws Exception {
        UtLoad.fromFileObject(loader, filename, charset);
    }

    /**
     * Загрузить из файла VFS в кодировке utf-8
     */
    public void fromFileObject(FileObject file) throws Exception {
        UtLoad.fromFileObject(loader, file);
    }

    /**
     * Загрузить из файла VFS в указанной кодировке
     */
    public void fromFileObject(FileObject file, String charset) throws Exception {
        UtLoad.fromFileObject(loader, file, charset);
    }

    /**
     * Загрузить из ресурса vfs. Если путь начинается с '/', то считается ресурсом
     */
    public void fromRes(String path, String charset) throws Exception {
        UtLoad.fromRes(loader, path, charset);
    }

    /**
     * Загрузить из ресурса vfs. Если путь начинается с '/', то считается ресурсом
     */
    public void fromRes(String path) throws Exception {
        UtLoad.fromRes(loader, path);
    }

}