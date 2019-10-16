package jandcode.core.web.virtfile.impl.virtfs;

import jandcode.core.web.virtfile.*;
import jandcode.core.web.virtfile.impl.*;

/**
 * Базовый класс для точек монтирования VirtFS
 */
public abstract class VirtFSMount extends BaseMount implements ITmlCheck {

    private ITmlCheck tmlCheck;

    /**
     * Установить проверяльщик расширений на предмет принадлежности к шаблонам
     */
    public void setTmlCheck(ITmlCheck tmlCheck) {
        this.tmlCheck = tmlCheck;
    }

    public boolean isTml(String ext) {
        if (tmlCheck == null) {
            return false;
        }
        return tmlCheck.isTml(ext);
    }

    /**
     * Проверить объект на правильность.
     * Вызывает VirtFS при добавлении точки.
     */
    public void validate() {
    }

    /**
     * Возвращает содержимое указанной виртуальной папки.
     * Всегда возвращается объект, даже если папка не найдена.
     *
     * @param path нормализованный путь до папки
     */
    public abstract FolderContent getFolderContent(String path);

}
