package jandcode.mdoc.web;

import jandcode.commons.*;
import jandcode.core.web.*;
import jandcode.core.web.render.*;
import jandcode.core.web.virtfile.*;
import jandcode.mdoc.builder.*;

public class OutFileRender implements IRender {

    private OutBuilder builder;
    private OutFile outFile;
    private boolean debug;

    public OutFileRender(OutBuilder builder, OutFile outFile, boolean debug) {
        this.builder = builder;
        this.outFile = outFile;
        this.debug = debug;
    }

    public void render(Object data, Request request) throws Exception {

        boolean saveDebug = builder.getMode().isDebug();
        try {
            builder.getMode().setDebug(debug);

            WebService svc = request.getApp().bean(WebService.class);
            FileType ft = svc.findFileType(UtFile.ext(outFile.getPath()));

            request.setContentType(ft.getMime());

            if (outFile.getTopic() != null) {
                outFile.getTopic().reload();
            }

            outFile.getSourceFile().copyTo(request.getOutStream());

        } finally {
            builder.getMode().setDebug(saveDebug);
        }

    }

}
