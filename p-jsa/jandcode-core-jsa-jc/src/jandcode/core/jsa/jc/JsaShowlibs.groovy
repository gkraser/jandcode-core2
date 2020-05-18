package jandcode.core.jsa.jc

import jandcode.commons.*
import jandcode.jc.*

class JsaShowlibs extends ProjectScript {

    protected void onInclude() throws Exception {
        super.onInclude()
        cm.add("jsa-showlibs", "Просмотр библиотек nodejs", this.&cmJsaShowlibs,
                cm.opt("c", "Только клиентские библиотеки"),
                cm.opt("q", "", "Выбирать только библиотеки, содержащие в имени подстроку"),
                cm.opt("a", "", "Вся информация"),
        )
    }

    void cmJsaShowlibs(CmArgs args) {
        boolean onlyClient = args.containsKey("c")
        boolean allInfo = args.containsKey("a")
        String nameFilter = null
        if (args.containsKey("q")) {
            nameFilter = args.getString("q")
        }

        NodeJsLibList libs = ctx.service(JsaService).getNodeJsLibs(project)
        Map res = [:]

        for (NodeJsLib lib : libs) {

            if (onlyClient && !lib.client) {
                continue
            }

            if (nameFilter != null) {
                if (!a.key.contains(nameFilter)) {
                    continue
                }
            }

            Map cur = [:]
            res[lib.name] = cur

            cur.version = lib.version
            if (lib.client) {
                cur.client = true
            }

            if (allInfo) {
                if (lib.includeClient.size() > 0) {
                    cur['include-client'] = UtString.join(lib.includeClient, ',')
                }
                if (lib.excludeClient.size() > 0) {
                    cur['exclude-client'] = UtString.join(lib.excludeClient, ',')
                }
                if (lib.excludeRequire.size() > 0) {
                    cur['exclude-require'] = UtString.join(lib.excludeRequire, ',')
                }
                if (lib.moduleMapping.size() > 0) {
                    List tmp = []
                    for (a in lib.moduleMapping) {
                        tmp.add(a.key + " => " + a.value)
                    }
                    cur['module-mapping'] = UtString.join(tmp, ',')
                }
            }
        }

        ut.printMap(res)
    }

}
