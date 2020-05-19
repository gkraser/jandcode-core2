package jandcode.core.jsa.jc

import jandcode.commons.*
import jandcode.jc.*

class JsaShowlibs extends ProjectScript {

    protected void onInclude() throws Exception {
        super.onInclude()
        cm.add("jsa-showlibs", "Просмотр библиотек nodejs", this.&cmJsaShowlibs,
                cm.opt("c", "Только клиентские библиотеки"),
                cm.opt("q", "", "Выбирать только библиотеки, содержащие в имени подстроку"),
                cm.opt("a", "", "Все библиотеки, без этой опции - только используемые в проекте"),
                cm.opt("i", "", "Показать всю информацию о библиотеке"),
                cm.opt("g", "", "Создать временный package.json с выбранными библиотеками"),
        )
    }

    void cmJsaShowlibs(CmArgs args) {
        boolean onlyClient = args.containsKey("c")
        boolean allInfo = args.containsKey("i")
        boolean allLibs = args.containsKey("a")
        boolean needPackageJson = args.containsKey("g")
        String nameFilter = null
        if (args.containsKey("q")) {
            nameFilter = args.getString("q")
        }

        NodeJsLibList libs
        if (allLibs) {
            libs = ctx.service(NodeJsLibService).getLibs()
        } else {
            libs = ctx.service(JsaService).getNodeJsLibs(project)
        }
        Map res = [:]
        NodeJsLibList libsChoiced = new NodeJsLibList()

        for (NodeJsLib lib : libs) {

            if (onlyClient && !lib.client) {
                continue
            }

            if (nameFilter != null) {
                if (!a.key.contains(nameFilter)) {
                    continue
                }
            }

            libsChoiced.add(lib)
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

        if (needPackageJson) {
            String outFile = wd("temp/jsa-showlibs/package.json")
            ut.cleanfile(outFile)
            log("save file: ${outFile}")
            JsaUtils jsaUtils = create(JsaUtils)
            JsaService jsaSvc = ctx.service(JsaService)
            Map<String, String> nodeDeps = jsaSvc.getNodeDepends(libsChoiced)
            String txt = jsaUtils.makePackageJson(nodeDeps)
            UtFile.saveString(txt, new File(outFile))
        }

    }

}
