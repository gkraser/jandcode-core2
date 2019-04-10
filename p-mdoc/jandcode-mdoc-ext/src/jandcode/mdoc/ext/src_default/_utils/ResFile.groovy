package jandcode.mdoc.ext.src_default._utils

import jandcode.commons.*
import jandcode.mdoc.cm.*

/**
 * Утилиты генерации для файлов из ресурсов
 */
class ResFile extends BaseCodeGen {

    /**
     * Возвращает файл из ресурсов.
     * @attr src путь в формате vfs (res:path/path...)
     */
    void path() {
        String fn = attrs.getString("src")
        String text = UtFile.loadString(fn)
        this.ext = UtFile.ext(fn)
        outText(text)
    }

}
