package jandcode.jc.std

import jandcode.jc.*
import jandcode.jc.impl.*

/**
 * Настрока проекта на наличие собственного _jc/registry-module-def.cfx.
 * Используется в не root-проектах, которые желают быть приложениями и
 * значит им нужен реестр модулей.
 */
class RegistryModuleDef extends ProjectScript {

    protected void onInclude() throws Exception {
        super.onInclude()
        //
        onEvent(PrepareProject.Event_Prepare, this.&prepareHandler)
    }

    void prepareHandler() {
        new ModuleDefProjectUtils(this).updateRegistryModuleDef()
    }

}
