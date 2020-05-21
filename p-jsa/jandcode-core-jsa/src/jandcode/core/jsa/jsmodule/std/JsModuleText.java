package jandcode.core.jsa.jsmodule.std;

import jandcode.commons.error.*;
import jandcode.core.jsa.cfg.*;
import jandcode.core.jsa.jsmodule.*;
import jandcode.core.jsa.jsmodule.impl.*;
import jandcode.core.web.*;
import jandcode.core.web.gsp.*;
import jandcode.core.web.virtfile.*;

/**
 * Базовый класс для модулей, загружаемых из текстовых файлов с возможной
 * компиляцией.
 */
public abstract class JsModuleText extends JsModuleImpl {

    private boolean compiledOnly = false;

    protected void onInit(JsModuleBuilder b) throws Exception {

        // оригинальный файл
        VirtFile f = b.getModule().getFile();

        String path = f.getPath();
        WebService webSvc = getApp().bean(WebService.class);

        boolean tmlBased = f.isTmlBased();

        if (tmlBased) {
            if (!f.getFileType().equals("gsp")) {
                throw new XError("Только gsp поддерживается для генерации модулей");
            }
            if (isCompiledOnly()) {
                throw new XError("Файл формата {0} нельзя генерировать, он требуют компиляции", f.getContentFileType());
            }
            // сгенерированный
            GspContext ctx = webSvc.createGspContext();
            ctx.getBeanFactory().registerBean(JsModuleBuilder.class.getName(), b);
            //
            ITextBuffer buf = ctx.render(path);
            b.setText(prepareTextAfterGsp(buf.toString()));
            //


        } else {
            JsaCfg jsaCfg = getApp().bean(JsaCfg.class);

            // обычный файл
            VirtFile compiledFile = null;
            if (jsaCfg.isMinify()) {
                // сначала ищем минифицированные, если нужно
                compiledFile = b.findCompiled("min");
            }
            if (compiledFile == null) {
                compiledFile = b.findCompiled(null);
            }
            if (compiledFile != null) {
                if (f.getLastModTime() > compiledFile.getLastModTime()) {
                    throw new XError("Файл требует перекомпиляции");
                }
                f = compiledFile;
                b.addModifyDepend(f.getPath());
            } else {
                if (isCompiledOnly()) {
                    throw new XError("Файл не скомпилирован");
                }
            }
            b.setText(f.loadText());
        }

    }

    public boolean isCompiledOnly() {
        return compiledOnly;
    }

    /**
     * true - только скомпилированные файлы можно использовать
     */
    public void setCompiledOnly(boolean compiledOnly) {
        this.compiledOnly = compiledOnly;
    }

    protected String prepareTextAfterGsp(String s) {
        return s;
    }

}
