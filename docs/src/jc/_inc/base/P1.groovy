package jc._inc.base


import jandcode.jc.*
import jandcode.jc.std.*

class P1 extends ProjectScript {

    void onInclude() {
        project.name = "tutorial-1"

        include(Utils)

        include(Utils).with {
            param1 = "NewValue1"
        }
        include(Utils).param2 = "Val2"

        Utils u = getIncluded(Utils)
        if (u != null) {
            u.param3 = "Q3"
        }

        include(Showlibs)

        cm.add("one", "Первая команда") {
            log("Команда 1 выполнена")
        }

        cm.add("two", "Вторая команда", this.&cmTwo,
                cm.opt("a", "Опция a"),
                cm.opt("b", "", "Опция b с параметром"),
        )

    }

    void cmTwo(CmArgs args) {
        ant.echo(message: "Arguments: ${args}")
        println("Печатаем текст")
        log("Команда 2 выполнена")
    }

    //////

    public static class Utils extends ProjectScript {
        String param1 = "value1"
        String param2 = "value2"
        String param3 = "value3"

        void onInclude() {
            cm.add("util-one", "Первая команда из утилит") {
                showParam()
                log("Команда util-one выполнена")
            }
        }

        void showParam() {
            log "Param1=${param1}"
            log "Param2=${param2}"
            log "Param3=${param3}"
        }
    }

}
