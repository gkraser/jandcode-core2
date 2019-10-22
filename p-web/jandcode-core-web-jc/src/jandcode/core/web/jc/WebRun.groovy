package jandcode.core.web.jc

import jandcode.commons.variant.*
import jandcode.core.jc.*
import jandcode.core.web.undertow.*
import jandcode.jc.*
import jandcode.jc.std.*

/**
 * Запуск web-приложения с командной строки
 */
class WebRun extends ProjectScript {

    protected void onInclude() throws Exception {
        cm.add("web-run", "Запуск web-приложения", this.&cmRun,
                cm.opt("p", null, "Порт (по умолчанию 8080)"),
                cm.opt("c", null, "Контекст (по умолчанию '/jc')"),
        )
    }

    //////

    void cmRun(IVariantMap args) {
        int port = args.getInt("p", 8080)
        String context = args.getString("c", "/jc")
        //

        // зависимости
        LibDepends deps = create(LibDependsUtils).getDepends(project)
        ctx.classpath(deps.all.libsAll)

        // подготовка исходников, в частности генерация registry-module-def.cfx
        include(AppProject).prepareSource()

        //
        UndertowRunner r = new UndertowRunner();
        r.setPort(port)
        r.setContext(context)

        println(ut.makeDelim(""))
        println(r.getUrl())
        println(ut.makeDelim(""))

        r.start();

        // останавливаемся тут, иначе jc доходит до остановки и сбрасывает ansi
        while (true) {
            Thread.sleep(10000000)
        }

    }

}