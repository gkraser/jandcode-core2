package jandcode.core.dbm.impl;

import jandcode.commons.error.*;
import jandcode.core.db.*;
import jandcode.core.db.std.*;

/**
 * Обертка вокруг Db.
 * Может функционировать в режиме авто-коннекта и авто-транзакция
 */
public class ModelDbWrapper extends BaseDbWrapper {

    private Db db;
    private boolean autoConnect;
    private boolean autoTran;

    /**
     * @param db          что обертываем
     * @param autoConnect true - автоматически установить соединение при необходимости
     * @param autoTran    true - автоматически стартовать транзакцию при
     *                    автоматической установки соединения
     */
    public ModelDbWrapper(Db db, boolean autoConnect, boolean autoTran) {
        this.db = db;
        this.autoConnect = autoConnect;
        this.autoTran = autoTran;
    }

    public ModelDbWrapper(Db db) {
        this(db, false, false);
    }

    protected Db getWrap() {
        return db;
    }

    protected Db getWrapConnected() {
        if (this.autoConnect && !db.isConnected()) {
            try {
                db.connect();
                if (this.autoTran) {
                    db.startTran();
                }
            } catch (Exception e) {
                throw new XErrorMark(e, "getWrapConnected");
            }
        }
        return db;
    }


}
