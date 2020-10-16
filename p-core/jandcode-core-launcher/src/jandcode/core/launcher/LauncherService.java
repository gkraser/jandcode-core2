package jandcode.core.launcher;

import jandcode.commons.named.*;
import jandcode.core.*;

/**
 * Сервис для launcher
 */
public interface LauncherService extends Comp {

    /**
     * При значении true требуется более подробный вывод сообщений.
     */
    boolean isVerbose();

    void setVerbose(boolean v);

    /**
     * Зарегистрированные команды
     */
    NamedList<Cmd> getCmds();

    /**
     * Найти команду по имени
     *
     * @param name имя
     * @return null, если нет такой команды
     */
    Cmd findCmd(String name);

}
