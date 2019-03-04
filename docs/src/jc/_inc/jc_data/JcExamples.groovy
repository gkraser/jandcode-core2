package jc._inc.jc_data

import jandcode.commons.vdir.*
import jandcode.jc.*
import jandcode.jc.std.*

class JcExamples extends ProjectScript {

    public void service1() throws Exception {
        JcDataService svc = ctx.service(JcDataService)

        // список всех файлов в каталоге
        List<String> lst = svc.findFiles("path1/path2")

        // найти файл
        String fn = svc.findFile("path1/file.txt")

        // ссылка на виртуальный каталог
        VDir vdir = svc.vdir
    }

}
