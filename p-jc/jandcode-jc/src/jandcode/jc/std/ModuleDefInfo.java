package jandcode.jc.std;

import jandcode.commons.moduledef.*;
import jandcode.commons.named.*;
import jandcode.jc.*;

/**
 * Описание для {@link ModuleDef} для модулей в исходниках.
 */
public interface ModuleDefInfo extends INamed {

    /**
     * Зависимости модуля.
     * Все зависимости попадают в зависимости java-модуля.
     * Зависимости, указанные в prod, транслируются в jandcode-модули и попадают
     * в зависимости определяемого модуля.
     */
    LibDepends getDepends();

    /**
     * {@link ModuleDef}, определяемый этим экземпляром
     */
    ModuleDef getModuleDef();

}
