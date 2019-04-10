package jandcode.commons.io.impl;

import jandcode.commons.*;
import jandcode.commons.io.*;
import org.xml.sax.*;

import java.io.*;

/**
 * Простой враппер для Reader с реализацией IReaderExt
 */
public class ReaderExtImpl extends Reader implements IReaderExt {

    protected Reader wrapped;
    protected String filename;
    protected String charset;
    protected InputStream stream;
    protected InputSource inputSource;

    public ReaderExtImpl(Reader wrapped, String filename, String charset, InputStream stream) {
        this.wrapped = wrapped;
        this.filename = filename;
        this.charset = charset;
        this.stream = stream;
    }

    public int read(char[] cbuf, int off, int len) throws IOException {
        return wrapped.read(cbuf, off, len);
    }

    public void close() throws IOException {
        wrapped.close();
    }

    public String getFilename() {
        return filename == null ? "" : filename;
    }

    public String getCharset() {
        return charset == null ? UtString.UTF8 : charset;
    }

    public InputSource getInputSource() {
        if (inputSource != null) {
            return inputSource;
        }
        if (stream == null) {
            inputSource = new InputSource(wrapped);
        } else {
            inputSource = new InputSource(stream);
        }
        return inputSource;
    }

}
