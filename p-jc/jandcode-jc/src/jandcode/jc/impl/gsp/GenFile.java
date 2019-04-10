package jandcode.jc.impl.gsp;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.io.*;
import org.apache.commons.io.output.*;

import java.io.*;

/**
 * Генерируемый файл
 */
public class GenFile {

    private GenContext genContext;
    private String filename;
    private boolean append;
    private IndentWriter writer;
    private String charset;

    public GenFile(GenContext genContext, String filename, boolean append, String charset) {
        this.genContext = genContext;
        this.filename = filename;
        this.append = append;
        this.charset = charset;
    }

    public String getFilename() {
        return filename;
    }

    public IndentWriter getWriter() {
        if (writer == null) {
            try {
                Writer w;
                if (genContext != null && genContext.isFakeMode()) {
                    w = new NullWriter();

                } else {
                    String dir = UtFile.path(filename);
                    UtFile.mkdirs(dir);
                    //
                    FileOutputStream f = new FileOutputStream(filename, append);
                    w = new OutputStreamWriter(f, charset == null ? UtString.UTF8 : charset);

                }
                writer = new IndentWriter(w);
            } catch (Exception e) {
                throw new XErrorWrap(e);
            }
        }
        return writer;
    }

    public void close() {
        if (writer != null) {
            try {
                writer.close();
            } catch (Exception e) {
                throw new XErrorWrap(e);
            }
            writer = null;
        }
    }

}
