package jandcode.core.dbm.dict.std;

import jandcode.commons.conf.*;
import jandcode.core.dbm.dict.*;
import jandcode.core.dbm.mdb.*;
import jandcode.core.store.*;

/**
 * Данные загружаются из свойства 'dictdata' в конфигурации словаря.
 * Например:
 * <pre>{@code
 * <dict name="yes-no" parent="base" handler="jandcode.core.dbm.dict.std.ConfDictHandler">
 *     <dictdata>
 *         <i id="1" text="YES"/>
 *         <i id="2" text="NO"/>
 *     </dictdata>
 * </dict>
 * }</pre>
 */
public class ConfDictHandler extends BaseDictHandlerLoadable {

    public void loadDict(Mdb mdb, Dict dict, Store data) throws Exception {
        Conf dd = dict.getConf().findConf("dictdata");
        if (dd != null) {
            for (Conf x : dd.getConfs()) {
                data.add(x);
            }
        }
    }

}
