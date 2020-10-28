package jandcode.jc.std

import jandcode.commons.*
import jandcode.commons.error.*
import jandcode.jc.*

/**
 * Сборка продукта
 */
class ProductProject extends ProjectScript {

    protected void onInclude() throws Exception {

        afterLoadAll {
            // собираем сборщики
            List lst = impl(ProductBuilder)

            if (lst.size() > 0) {
                // есть сборщики
                cm.add("product", "Сборка продукта", this.&cmProduct,
                        cm.opt("q", false, "Быстрая сборка. Пропустить этап компиляции (для отладки)"),
                        cm.opt("dev", false, "Dev-сборка. Не устанавливается флаг ctx.env.prod"),
                        cm.opt("u", false, "Обновление сборки. Собирает, если изменилась версия"),
                        cm.opt("l", false, "Показать сборщики"),
                )
                if (lst.size() > 1) {
                    cm.get("product").opts.add(
                            cm.opt("b", "", "Какие сборщики запускать, через ','. Если не указано - выполняются все"),
                    )
                }
            }
        }

    }

    void cmProduct(CmArgs args) {
        List builders = impl(ProductBuilder)

        //
        boolean quick = args.containsKey("q")
        boolean dev = args.containsKey("dev")
        boolean update = args.containsKey("u")
        boolean showBuilders = args.containsKey("l")

        if (showBuilders) {
            Map res = [:]
            int n = 0
            for (b in builders) {
                n++
                res['' + n] = [
                        name   : b.name,
                        'class': b.getClass().getName()
                ]
            }
            ut.printMap(res)
            return
        }

        if (!dev) {
            // production mode
            ctx.env.prod = true
        }


        Set<String> needs = null
        if (args.containsKey("b")) {
            needs = new HashSet<>(UtCnv.toList(args.get("b")))
        }

        int cnt = 0
        for (ProductBuilder b : builders) {
            if (needs == null || needs.contains(b.name)) {
                cnt++
                b.quick = quick
                b.dev = dev
                b.update = update
                b.args.clear()
                b.args.putAll(args)
                b.exec()
            }
        }
        if (needs != null && cnt == 0) {
            throw new XError("Не выполненен ни один сборщик продукта по заданным условиям")
        }

    }

    /**
     * Найти builder по имени
     */
    ProductBuilder findBuilder(String name) {
        List builders = impl(ProductBuilder)
        for (ProductBuilder b : builders) {
            if (b.name == name) {
                return b
            }
        }
        return null
    }

}
