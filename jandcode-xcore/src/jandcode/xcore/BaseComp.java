package jandcode.xcore;

import jandcode.commons.*;
import jandcode.xcore.impl.*;

/**
 * Базовый предок для компонентов приложения {@link Comp}.
 */
public abstract class BaseComp extends CompImpl implements Comp {

    /**
     * Реализация конфигурирования компонента
     * При перекрытии не забудьте вызвать super.
     */
    protected void onConfigure(BeanConfig cfg) throws Exception {
        UtReflect.getUtils().setProps(this, cfg.getConf());
    }

}
