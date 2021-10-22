package jandcode.core.nodejs.jc


import jandcode.commons.named.*
import jandcode.jc.*

class NodeJsShowdeps extends ProjectScript {

    protected void onInclude() throws Exception {
        cm.add("nodejs-showdeps", "Просмотр зависимостей npm", this.&cmNodeJsShowdeps,
                cm.opt("q", "", "Выбирать только модули, содержащие в имени подстроку"),
                cm.opt("e", false, "Показать только перекрытые библиотеки из числа запрошенных"),
        )
    }

    void cmNodeJsShowdeps(CmArgs args) {
        String nameFilter = null
        if (args.containsKey("q")) {
            nameFilter = args.getString("q")
        }
        boolean overrideFilter = args.containsKey("e")

        Map res = [:]
        NodeJsUtils nut = create(NodeJsUtils)

        // все известные модули
        NamedList<NodeJsModule> allModules = ctx.service(NodeJsService).getModules()
        // все зависимости npm, которые используются
        Map allDeps = nut.grabAllDepends()


        for (dep in allDeps) {

            String depName = dep.key
            String depVersion = dep.value

            if (nameFilter != null) {
                if (!depName.contains(nameFilter)) {
                    continue
                }
            }

            Map cur = [:]

            cur.version = depVersion

            List used = []
            for (module in allModules) {
                def depVersionModule = module.getAllDependencies().get(depName)
                if (!depVersionModule) {
                    continue
                }
                used.add("${module.name}: ${depVersionModule}")
            }

            cur.usedInModules = used.join(', ')
            if (used.size() > 1) {
                cur.override = true
            }

            if (overrideFilter && !cur.override) {
                continue
            }

            res[depName] = cur
        }

        ut.printMap(res)

    }

}
