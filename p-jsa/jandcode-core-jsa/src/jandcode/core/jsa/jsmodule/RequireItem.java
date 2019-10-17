package jandcode.core.jsa.jsmodule;

import java.util.*;

/**
 * Описание require из текста модуля
 */
public interface RequireItem {

    /**
     * Имя в require, например: require('./mymodule')
     */
    String getUsedName();

    /**
     * Список имен реальных модулей, которые соотвествуют указанном в getUsedName(),
     * например: 'my/path/mymodule.js'
     */
    List<String> getRealNames();

}
