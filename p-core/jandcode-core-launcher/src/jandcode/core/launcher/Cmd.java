package jandcode.core.launcher;

import jandcode.commons.named.*;

/**
 * Описание команды
 */
public interface Cmd extends INamed {

    /**
     * Помощь по команде
     */
    Help getHelp();

    /**
     * Создать экземпляр команды
     */
    LauncherCmd createInst();

}
