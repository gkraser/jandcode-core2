package jandcode.jc.projects

import jandcode.jc.*

class project1_project3 extends ProjectScript {
    protected void onInclude() throws Exception {
        //==========================================================================

        println "Included"
        println "ProjectName    = ${name}"
        println "WD             = ${wd}"
        println "WD(temp)       = ${wd('temp')}"

        //==========================================================================
    }
}
