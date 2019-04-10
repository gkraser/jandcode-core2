package jandcode.web.virtfile.impl.render;

import jandcode.commons.*;
import jandcode.web.virtfile.*;

import java.io.*;

/**
 * render файла как бинарного потока.
 */
public class BinVirtFileRender extends BaseVirtFileRender {

    protected void onRenderFile(VirtFile f, FileType fileType, FileType contentFileType) throws Exception {
        getRequest().setContentType(contentFileType.getMime());
        //
        getRequest().setHeader("Content-Length", UtCnv.toString(f.getSize()));
        InputStream stmSource = f.getInputStream();
        OutputStream stmDest = getRequest().getOutStream();
        try {
            UtFile.copyStream(stmSource, stmDest);
        } finally {
            stmSource.close();
        }
    }

}
