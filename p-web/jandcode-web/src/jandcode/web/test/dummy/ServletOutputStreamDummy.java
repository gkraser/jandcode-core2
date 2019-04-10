package jandcode.web.test.dummy;


import javax.servlet.*;
import java.io.*;

public class ServletOutputStreamDummy extends ServletOutputStream {

    private ByteArrayOutputStream _outStream = new ByteArrayOutputStream();

    public void write(int b) throws IOException {
        _outStream.write(b);
    }

    public String getString() {
        String s = _outStream.toString();
        if (s == null) {
            s = "";
        }
        return s;
    }

    //////

    public boolean isReady() {
        return true;
    }

    public void setWriteListener(WriteListener writeListener) {
    }

}
