package jandcode.web.jc


import jandcode.commons.simxml.*
import jandcode.jc.*
import jandcode.jc.std.*
import jandcode.jc.std.idea.*

/**
 * Генерирует конфигурацию запуска tomcat из idea
 */
class GenIdea_RunTomcat extends ProjectScript {

    /**
     * Имя сервера tomcat, зарегистрированного в idea
     */
    String tomcatServerName = "jc-tomcat"

    /**
     * Список библиотек, которые исключаются из артефакта
     */
    List<String> excludeLibs = ["javax.servlet-api"]

    protected void onInclude() throws Exception {
        //
        include(WebDir)

        //
        include(GenIdea)
        onEvent(GenIdea.Event_GenIpr, this.&genIprHandler)
        onEvent(GenIdea.Event_GenIml, this.&genImlHandler)
    }

    void genIprHandler(GenIdea.Event_GenIpr e) {
        IprXml x = e.x

        String artifactName = "jc-war"

        // run
        SimXml z = x.addRunConfig("run tomcat", ctx.service(JcDataService).getFile("idea/run-tomcat-template.xml"))
        z['APPLICATION_SERVER_NAME'] = tomcatServerName

        // artifact
        SimXml x1 = x.root.findChild("component@name=ArtifactManager/artifact@name=${artifactName}", true)
        x1["type"] = "exploded-war"
        x1["build-on-make"] = false  // что бы не собирался при каждой сборке!
        x1['output-path:#text'] = "\$PROJECT_DIR\$/out/artifacts/${artifactName}"

        SimXml x2 = x1.findChild("root@id=root", true)
        x2.clearChilds()

        SimXml x3 = x2.addChild("element")
        x3["id"] = "directory"
        x3["name"] = "WEB-INF"

        // зависимости
        LibDepends deps = create(LibDependsUtils).getDepends(project)
        def libs = deps.all.libsAll

        // модули
        SimXml x4 = x3.addChild("element")
        x4["id"] = "directory"
        x4["name"] = "classes"
        for (lib in libs) {
            if (lib.sourceProject == null) {
                continue
            }
            if (excludeLibs.contains(lib.name)) {
                continue
            }
            SimXml xm = x4.addChild("element")
            xm["id"] = "module-output"
            xm["name"] = lib.name
        }

        // jars
        x4 = x3.addChild("element")
        x4["id"] = "directory"
        x4["name"] = "lib"
        for (lib in libs) {
            if (lib.sourceProject != null) {
                continue
            }
            if (excludeLibs.contains(lib.name)) {
                continue
            }
            SimXml xm = x4.addChild("element")
            xm["id"] = "library"
            xm["level"] = "module"
            xm["name"] = lib.name
            xm["module-name"] = project.name
        }

        // facet
        x3 = x2.addChild("element")
        x3["id"] = "javaee-facet-resources"
        x3["facet"] = "${project.name}/web/Web"

    }

    void genImlHandler(GenIdea.Event_GenIml e) {
        ImlXml x = e.x

        SimXml x1 = x.root.findChild("component@name=FacetManager/facet@name=Web", true)
        x1["type"] = "web"
        x1["configuration/descriptors/deploymentDescriptor@name=web.xml:url"] = "file://\$MODULE_DIR\$/web/WEB-INF/web.xml"

        SimXml x2 = x1.findChild("configuration/webroots", true)
        x2.clearChilds()

        SimXml x3 = x2.addChild("root")
        x3["url"] = "file://\$MODULE_DIR\$/web"
        x3["relative"] = "/"
    }

}
