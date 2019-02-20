package jandcode.xcore;

import jandcode.commons.named.*;

/**
 * Компонент приложения.
 * Компонент имеет имя (регистронезависимое) и владеет ссылкой на приложение,
 * в контексте которого он работает.
 */
public interface Comp extends INamed, INamedSet, IAppLink, IAppLinkSet, IBeanConfigure {
}
