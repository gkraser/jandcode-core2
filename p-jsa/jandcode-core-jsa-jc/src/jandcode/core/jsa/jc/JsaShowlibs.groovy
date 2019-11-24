package jandcode.core.jsa.jc

import jandcode.commons.named.*
import jandcode.jc.*

class JsaShowlibs extends ProjectScript {

    protected void onInclude() throws Exception {
        super.onInclude()
        cm.add("jsa-showlibs", "Просмотр библиотек node", this.&cmJsaShowlibs,
                cm.opt("c", "Только клиентские библиотеки"),
                cm.opt("q", "", "Выбирать только библиотеки, содержащие в имени подстроку"),
                cm.opt("e", false, "Показать перекрытые библиотеки из числа запрошенных"),
        )
    }

    void cmJsaShowlibs(CmArgs args) {
        boolean onlyClient = args.containsKey("c")
        String nameFilter = null
        if (args.containsKey("q")) {
            nameFilter = args.getString("q")
        }
        boolean onlyOverride = args.containsKey("e")

        NamedList<JsaModule> moduleInfos = ctx.service(JsaService).getJsaModules(project)
        Map res = [:]

        for (JsaModule m : moduleInfos) {

            Map nodeDepends = m.getNodeDepends()
            for (nd in nodeDepends) {
                String name = nd.key
                String vers = nd.value

                Map cur = res[name]
                if (cur == null) {
                    cur = [:]
                    res[name] = cur
                    //
                    cur.version = vers
                    cur.module = m.name
                } else {
                    if (cur.version instanceof String) {
                        cur.version = [cur.version]
                    }
                    if (cur.module instanceof String) {
                        cur.module = [cur.module]
                    }
                    cur.version.add(vers)
                    cur.module.add(m.name)
                }
                if (isClientLib(m, name)) {
                    cur.client = true
                }
            }
        }

        if (nameFilter != null) {
            Map tmp = [:]
            for (a in res) {
                if (!a.key.contains(nameFilter)) {
                    continue
                }
                tmp[a.key] = a.value
            }
            res = tmp
        }

        if (onlyClient) {
            Map tmp = [:]
            for (a in res) {
                if (!a.value.client) {
                    continue
                }
                tmp[a.key] = a.value
            }
            res = tmp
        }

        if (onlyOverride) {
            Map tmp = [:]
            for (a in res) {
                if (a.value.version instanceof List) {
                    tmp[a.key] = a.value
                }
            }
            res = tmp
        }

        ut.printMap(res)
    }

    boolean isClientLib(JsaModule m, String libName) {
        Map gt = m.getGulpTasks()['nm']
        if (gt == null) {
            return false;
        }
        String pfx = libName + '/'
        for (String mask in gt.globs) {
            if (mask.startsWith(pfx) || mask.startsWith('!' + pfx)) {
                return true
            }
        }
    }

}
