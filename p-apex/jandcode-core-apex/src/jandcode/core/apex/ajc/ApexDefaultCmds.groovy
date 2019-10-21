package jandcode.core.apex.ajc

class ApexDefaultCmds extends ApexJcScript {

    protected void onInclude() throws Exception {

        include(ApexAppManager)
        include(ApexWebCmds)

        cm.add("apex1") {
            log "hello apex!"
        }
    }
}
