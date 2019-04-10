package jandcode.jc.std

import jandcode.jc.*
import jandcode.jc.impl.depends.*

/**
 * Утилиты для LibDepends
 */
class LibDependsUtils extends ProjectScript {

    /**
     * Возвращает все зависимости из проекта
     */
    LibDepends getDepends(Project p) {
        LibDepends dep = new LibDependsImpl(ctx, this)
        if (p == null) {
            return dep;
        }
        for (ILibDepends z : p.impl(ILibDepends)) {
            for (LibDependsGroup g : z.getDepends().getGroups()) {
                dep.addGroup(g.getName()).add(g.getNames())
            }
        }
        for (ILibDependsGrab z : p.impl(ILibDependsGrab)) {
            z.grabDepends(dep)
        }
        return dep;
    }


}
