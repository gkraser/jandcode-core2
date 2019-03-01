package jc._inc.gen_gsp

import jandcode.jc.*

class GenExample extends ProjectScript {

    public void example1() throws Exception {
        //= example1

        // сюда будем генерировать
        def outDir = wd("temp/gendir1")

        // ощищаем каталог, он будет пустым и существующим
        ut.cleandir(outDir)

        // создаем скрипт генератор по gsp-файлу
        GspScript gs = create("my-templates/template1.gsp")

        // запускаем генерацию, указывая куда генерировать и с какими аргументами
        gs.generate("${outDir}/result1.txt", [a1: 1, a2: 2])

        //=
    }

    public void example_for_changeFile1() throws Exception {
        //= example_for_changeFile1
        // создаем скрипт генератор по gsp-файлу
        GspScript gs = create("my-templates/template1.gsp")

        // запускаем генерацию, указывая файл, куда генерировать
        // каталог с этим файлом будет базовым каталогом генерации
        gs.generate("temp/gendir/result1.txt", [a1: 1, a2: 2])
        //=
    }


}
