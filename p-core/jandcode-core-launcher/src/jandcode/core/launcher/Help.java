package jandcode.core.launcher;

import java.util.*;

/**
 * Помощь по опциям
 */
public interface Help {

    /**
     * Список опций
     */
    Collection<Opt> getOpts();

    /**
     * Описание набора опций
     */
    String getDesc();

}
