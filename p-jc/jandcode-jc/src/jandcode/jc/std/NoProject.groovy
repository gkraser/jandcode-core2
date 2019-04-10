package jandcode.jc.std

import jandcode.jc.*

/**
 * Скрипт для включения в проект, при запуске jc в каталоге,
 * в котором нет файла project.jc
 */
class NoProject extends ProjectScript {
    protected void onInclude() throws Exception {
        // включаем команду создания проекта
        include(CreateProject)
        // включаем showinfo
        include(Showinfo)
        // включаем showinfo
        include(Showlibs)
    }
}
