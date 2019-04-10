package jandcode.jc.junit

import jandcode.commons.*
import jandcode.jc.*
import jandcode.jc.std.*
import jandcode.jc.std.junit.*

class JUnitRunner extends ProjectScript implements IJUnitRunner {

    /**
     * Запуск unit-тестов
     */
    boolean runTests(String testClassName, String testMethods) {
        JUnitProject ju = getIncluded(JUnitProject)
        if (ju == null) {
            error("JUnitProject не включен")
        }

        JavaProject jm = getIncluded(JavaProject)
        if (jm == null) {
            error("JavaProject не включен")
        }

        boolean testExecFail = false

        def rlibs = ctx.getLibs(jm.depends.all.libs.names + [project.name] +
                [
                        "junit-platform-commons",
                        "junit-platform-engine",
                        "junit-platform-launcher",
                        "opentest4j",
                        "junit-jupiter-api",
                        "junit-jupiter-engine",
                        "junit-platform-console",
                        "jandcode-jc-junit",
                ]
        )
        def cp = rlibs.classpath
        cp.add(jm.dirCompiledTest)
        String cp_s = UtString.join(cp, ";")

        String repXmlDir = wd(ju.dirTestreportXml)
        ut.cleandir(repXmlDir)

        ut.stopwatch.start("run tests")
        try {
            String cn = "jandcode.jc.junit.ConsoleLauncherWrap"

            String appArgs = ""
            appArgs += " --details=none"
            appArgs += " --reports-dir=${repXmlDir}"
            if (UtString.empty(testClassName)) {
                appArgs += " --scan-classpath=${jm.dirCompiledTest}"
            } else {
                if (!UtString.empty(testMethods)) {
                    List<String> lstMethods = UtCnv.toList(testMethods)
                    for (mn in lstMethods) {
                        appArgs += " --select-method=${testClassName}#${mn}"
                    }
                } else {
                    appArgs += " --select-class=${testClassName}"
                }
            }
            appArgs += " --config=junit.platform.output.capture.stdout=true"
            appArgs += " --config=junit.platform.output.capture.stderr=true"
            appArgs += " --config=junit.jupiter.extensions.autodetection.enabled=true"

            String jvmArgs = ""
            jvmArgs += " -cp ${cp_s}"

            String cmd = "java ${jvmArgs} ${cn} ${appArgs}"

            try {
                ut.runcmd(cmd: cmd)
            } catch (e) {
                testExecFail = true
            }

        } finally {
            ut.stopwatch.stop("run tests")
        }

        return testExecFail
    }


}
