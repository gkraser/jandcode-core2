package jandcode.mdoc.web;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.variant.*;
import jandcode.core.*;
import jandcode.core.std.*;
import jandcode.core.web.*;
import jandcode.core.web.action.*;
import jandcode.mdoc.*;
import jandcode.mdoc.builder.*;
import jandcode.mdoc.source.*;

public class MDocAction extends BaseAction {

    class MDocParams {
        final OutBuilder builder;
        String outFileName;
        OutFile outFile;
        String sourceFileName;
        SourceFile sourceFile;

        public MDocParams(Request req) {
            IVariantMap p = req.getParams();
            this.builder = getApp().bean(WebMDocService.class).getBuilder(p.getString("builder", "default"));

            //
            this.outFileName = p.getString("outFile");
            if (!UtString.empty(this.outFileName)) {
                this.outFile = this.builder.getOutFiles().findByPath(this.outFileName);
                if (this.outFile == null) {
                    throw new XError("Файл не найден: " + this.outFileName);
                }
            }

            //
            this.sourceFileName = p.getString("sourceFile");
            if (!UtString.empty(this.sourceFileName)) {
                this.sourceFile = this.builder.getDoc().getSourceFiles().find(this.sourceFileName);
                if (this.sourceFile == null) {
                    throw new XError("Файл не найден: " + this.sourceFileName);
                }
            }
        }

    }

    public void index() throws Exception {
        getReq().redirect("/mdoc/out");
    }

    public void out() throws Exception {
        // todo
        // проверяем изменения в ресурсах, т.к. gsp могут поменятся, а в prod это
        // не делается
        getApp().bean(CheckChangedResourceService.class).checkChangedResource();
        //

        MDocParams p = new MDocParams(getReq());
        synchronized (p.builder) {
            String path = getReq().getActionMethodPathInfo();
            if (UtString.empty(path)) {
                getReq().redirect("/index.html");
            }

            OutFile fo;

            if (path.startsWith("-debug/page-") && p.builder instanceof HtmlOutBuilder) {
                String fn = UtFile.removeExt(UtFile.filename(path));
                String mdFile = UtFile.removeExt(path) + ".md";
                // сервисная страница
                String mdText = "" +
                        "---\n" +
                        "contentTemplate: _theme/debug/" + fn + ".gsp\n" +
                        "---\n";
                fo = ((HtmlOutBuilder) p.builder).createOutFile(mdFile, mdText);

            } else {
                fo = p.builder.getOutFiles().findByPath(path);
                if (fo == null) {
                    throw new HttpError(404);
                }
            }
            boolean prod = getReq().getParams().containsKey("prod");
            getReq().render(new OutFileRender(p.builder, fo, !prod));
        }
    }

    public void cmEdit() throws Exception {
        BeanDef cmdRunnerBean = getApp().getBeanFactory().findBean(CmdRunner.class.getName());
        if (cmdRunnerBean == null) {
            throw new XError("Не настроен {0}", CmdRunner.class.getName());
        }

        MDocParams p = new MDocParams(getReq());
        synchronized (p.builder) {
            SourceFile sf = p.sourceFile;
            if (UtString.empty(sf.getRealPath())) {
                throw new XError("Файл " + sf.getPath() + " не знает про свой реальный путь");
            }
            String filename = UtFile.vfsPathToLocalPath(sf.getRealPath());
            filename = filename.replace("/", "\\");
            int lineNumber = getReq().getInt("lineNumber");
            String sourcePos = getReq().getString("sourcePos");
            if (!UtString.empty(sourcePos)) {
                String ar[] = sourcePos.split("-");
                if (ar.length == 2) {
                    lineNumber = UtString.lineNum(sf.getText().replace("\r", ""), UtCnv.toInt(ar[0])) + 1;
                }
            }
            ((CmdRunner) cmdRunnerBean.getInst()).runEditor(filename, lineNumber);
            getReq().render(UtCnv.toMap("result", true));
        }
    }

    public void cmHasChangedSinceTime() throws Exception {
        MDocParams p = new MDocParams(getReq());
        synchronized (p.builder) {
            boolean flag = p.builder.hasChangedSinceTime(p.outFile, getReq().getLong("time"));
            getReq().render("" + flag);
        }
    }

    public void cmRebuild() throws Exception {
        MDocParams p = new MDocParams(getReq());
        synchronized (p.builder) {
            Doc newDoc = getApp().bean(MDocService.class).createDocument(p.builder.getOriginalDoc().getCfg());
            newDoc.load();
            OutBuilder newBuilder = newDoc.createBuilder("html");  // todo тут нужно создавать такой же, какой был

            newBuilder.getMode().setServe(true);
            newBuilder.getMode().setDebug(p.builder.getMode().isDebug());

            newBuilder.build();
            getApp().bean(WebMDocService.class).registerBuilder("default", newBuilder);
            getReq().render("ok");
        }
    }

}
