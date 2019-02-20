package jandcode.commons.io.impl;

import jandcode.commons.*;
import jandcode.commons.io.*;

import java.io.*;

/**
 * Простой враппер для Writer с реализацией IWriterExt
 */
public class WriterExtImpl extends Writer implements IWriterExt {

    protected Writer wrapped;
    protected String filename;
    protected String charset;

    public WriterExtImpl(Writer wrapped, String filename, String charset) {
        this.wrapped = wrapped;
        this.filename = filename;
        this.charset = charset;
    }

    public void write(char[] cbuf, int off, int len) throws IOException {
        wrapped.write(cbuf, off, len);
    }

    public void flush() throws IOException {
        wrapped.flush();
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

}
