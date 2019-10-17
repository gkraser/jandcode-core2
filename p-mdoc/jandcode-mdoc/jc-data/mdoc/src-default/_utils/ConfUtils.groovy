import jandcode.commons.*
import jandcode.commons.conf.*
import jandcode.commons.test.*
import jandcode.mdoc.cm.*
import jandcode.mdoc.source.*

class ConfUtils extends BaseCodeGen {

    /**
     * Из файла cfx делает текст загруженного Conf
     * @attr src исходный cfx
     */
    void confToText() {
        Conf x = UtConf.create()
        SourceFile f = attrs.getSourceFile("src")
        UtConf.load(x).fromString(f.getText(), f.getPath())
        ext = "js"
        outText(new OutMapSaver(x).save().toString())
    }

}
