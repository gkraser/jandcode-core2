package jandcode.core.nodejs.jc


import jandcode.commons.named.*
import jandcode.jc.*

class NodeJsShowlibs extends ProjectScript {

    protected void onInclude() throws Exception {
        super.onInclude()
        cm.add("nodejs-showlibs", "Просмотр библиотек nodejs", this.&cmNodeJsShowlibs,
                cm.opt("q", "", "Выбирать только библиотеки, содержащие в имени подстроку"),
        )
    }

    void cmNodeJsShowlibs(CmArgs args) {
        String nameFilter = null
        if (args.containsKey("q")) {
            nameFilter = args.getString("q")
        }

        // все известные модули
        NamedList<NodeJsModule> allModules = ctx.service(NodeJsService).getModules()
        Map res = [:]
        NamedList<NodeJsModule> choicedModules = new DefaultNamedList<>()


        for (NodeJsModule module : allModules) {

            if (nameFilter != null) {
                if (!module.name.contains(nameFilter)) {
                    continue
                }
            }

            choicedModules.add(module)
            Map cur = [:]
            res[module.name] = cur

            cur.version = module.version
            cur.path = module.path

            if (module.ownerProject != null) {
                cur.project = module.ownerProject.projectFile
            }

        }

        ut.printMap(res)

    }

}
