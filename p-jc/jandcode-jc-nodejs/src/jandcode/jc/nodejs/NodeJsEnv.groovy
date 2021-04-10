package jandcode.jc.nodejs


import jandcode.jc.*
import jandcode.jc.std.*

/**
 * Среда nodejs. Сборник всех внешних зависимостей, настройка node_modules,
 * алиасов модулей.
 */
class NodeJsEnv extends ProjectScript {

    protected void onInclude() throws Exception {
        include(NodeJsShowlibs)

        // prepare
        include(PrepareProject)
        onEvent(PrepareProject.Event_Prepare, this.&prepareHandler)
    }

    void prepareHandler() {
        NodeJsUtils nut = create(NodeJsUtils)

        log "nodejs prepare for: ${name}"
        ant.mkdir(dir: wd(JcConsts.JC_METADATA_DIR))

        // prepare-data
        List<String> pdd = ctx.service(JcDataService).vdir.getRealPathList("nodejs/prepare-data")
        String pdd_dest = nut.getMetaDataPath(nut.PATH_PREPARE)
        ant.mkdir(dir: pdd_dest)
        for (String pdd1 : pdd) {
            ant.copy(todir: pdd_dest, overwrite: true) {
                fileset(dir: pdd1)
            }
        }

        // jc-env.bat
        String s = nut.makeJcEnvBat()
        JcEnvBuilder jcEnvBuilder = include(JcEnvBuilder)
        jcEnvBuilder.append(s)

        // jc-nodejs-modules.js
        s = nut.makeJcNodejsModulesJs()
        ant.echo(message: s, file: nut.getMetaDataPath(nut.PATH_JC_NODEJS_MODULES))

        // webpack dummy
        s = nut.makeWebpackDummy()
        ant.echo(message: s, file: nut.getMetaDataPath(nut.PATH_WEBPACK_DUMMY))
        ant.echo(message: "//", file: nut.getMetaDataPath(nut.PATH_WEBPACK_DUMMY_ENTRY))

        // package.json
        nut.updatePackageJson(nut.getMetaDataPath(nut.PATH_PACKAGE_JSON))

        // node_modules
        nut.updateNodeModules(nut.getMetaDataPath(nut.PATH_PACKAGE_JSON))

    }

}
