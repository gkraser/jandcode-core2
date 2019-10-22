package jandcode.core.apex.ajc

/**
 * Команды по умолчанию
 */
class ApexDefaultCmds extends ApexJcScript {

    protected void onInclude() throws Exception {
        include(ApexAppManager)
        include(ApexWebCmds)
    }

}
