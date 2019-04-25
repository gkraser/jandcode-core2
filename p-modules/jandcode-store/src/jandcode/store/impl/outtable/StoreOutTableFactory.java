package jandcode.store.impl.outtable;

import jandcode.commons.outtable.*;
import jandcode.store.*;

public class StoreOutTableFactory implements OutTableFactory {

    public OutTable createOutTable(Object data) {
        if (data instanceof Store) {
            return new StoreOutTable((Store) data);
        }
        if (data instanceof StoreRecord) {
            return new StoreOutTable((StoreRecord) data);
        }
        return null;
    }

}
