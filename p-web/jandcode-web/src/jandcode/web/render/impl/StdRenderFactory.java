package jandcode.web.render.impl;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.core.*;
import jandcode.web.*;
import jandcode.web.render.*;
import jandcode.web.type.*;

/**
 * Стандартная фабрика render.
 * Имя render берется из атрибута 'render' типа {@link TypeDef}.
 * Если data реализует интерфейс render, то data и считается как render.
 */
public class StdRenderFactory extends BaseComp implements IRenderFactory {

    public IRender createRender(Object data, Request request) {
        WebService svc = getApp().bean(WebService.class);
        //
        if (data == null) {
            return null;
        }
        if (data instanceof IRender) {
            // data реализует render - возвращаем его
            return (IRender) data;
        }
        TypeDef t = svc.findType(data.getClass());
        if (t == null) {
            return null;
        }
        String rn = t.getAttr("render");
        if (UtString.empty(rn)) {
            return null;
        }
        RenderDef rnd = svc.getRenders().find(rn);
        if (rnd == null) {
            throw new XError("Для класса {0} указан не существующий render {0}", data.getClass(), rn);
        }
        return rnd.createInst();
    }

}
