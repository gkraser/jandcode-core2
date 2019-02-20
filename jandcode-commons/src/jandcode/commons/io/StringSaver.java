package jandcode.commons.io;

import java.io.*;

/**
 * Saver для записи строки в разные приемники
 */
public class StringSaver implements ISaver {

    private String data;

    public StringSaver(String data) {
        this.data = data;
    }

    public SaveTo save() {
        return new SaveTo(this);
    }

    public void saveTo(Writer writer) throws Exception {
        if (data != null) {
            writer.write(data);
        }
    }
}
