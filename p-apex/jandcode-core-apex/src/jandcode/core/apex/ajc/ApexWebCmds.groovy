package jandcode.core.apex.ajc

import jandcode.core.*
import jandcode.core.web.undertow.*
import jandcode.jc.*

class ApexWebCmds extends ApexJcScript {

    protected void onInclude() throws Exception {

        cm.add("web-run", "Запуск web-приложения", this.&cmWebRun,
                cm.opt("p", null, "Порт (по умолчанию 8080)"),
                cm.opt("c", null, "Контекст (по умолчанию '/jc')"),
        )

    }

    void cmWebRun(CmArgs args) {
        App app = getApp()

        int port = args.getInt("p", 8080)
        String context = args.getString("c", "/jc")

        //
        UndertowRunner r = new UndertowRunner();
        r.setPort(port)
        r.setContext(context)

        println(ut.makeDelim(""))
        println(r.getUrl())
        println(ut.makeDelim(""))

        r.start(app);

        // останавливаемся тут, иначе jc доходит до остановки и сбрасывает ansi
        while (true) {
            Thread.sleep(10000000)
        }

    }


}
