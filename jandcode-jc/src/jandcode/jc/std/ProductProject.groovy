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

        // production mode
        ctx.env.prod = true

        //
        boolean quick = args.containsKey("q")

        Set<String> needs = null
        if (args.containsKey("b")) {
            needs = new HashSet<>(UtCnv.toList(args.get("b")))
        }

        int cnt = 0
        for (ProductBuilder b : builders) {
            if (needs == null || needs.contains(b.name)) {
                cnt++
                b.quick = quick
                b.args.clear()
                b.args.putAll(args)
                b.exec()
            }
        }
        if (needs != null && cnt == 0) {
            throw new XError("Не выполненен ни один сборщик продукта по заданным условиям")
        }

    }

}
