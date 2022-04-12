package jandcode.core.dbm.genid.std;

import jandcode.commons.*;
import jandcode.commons.error.*;
import jandcode.commons.named.*;
import jandcode.commons.variant.*;
import jandcode.core.db.*;
import jandcode.core.dbm.*;
import jandcode.core.dbm.genid.*;
import jandcode.core.dbm.mdb.*;
import org.slf4j.*;

import java.util.*;

/**
 * Разные инструменты для генераторов
 */
public class GenIdTools implements IModelLink {

    protected static Logger log = LoggerFactory.getLogger(GenIdService.class);

    private Model model;

    public GenIdTools(Model model) {
        this.model = model;
    }

    public Model getModel() {
        return model;
    }

    /**
     * Восстановить состояние всех генераторов в соответствии с текущим положением в базе данных.
     * Используется обычно только в цикле разработки/установки.
     * <p>
     * Ошибки игнорируются и выводятся в Log.
     */
    public void recoverGenIds() throws Exception {
        recoverGenIds(null, false);
    }

    /**
     * Восстановить состояние указанных генераторов в соответствии с текущим положением в базе данных.
     * Используется обычно только в цикле разработки/установки.
     * <p>
     * Подразумевается, что для генератора имеется таблица с именем как у генератора,
     * и в этой таблице имеется поле id, значение которого было сгенерировано генератором.
     *
     * @param genIdNames имена генераторов, которые нужно обработать. Если null, обрабатываются все
     * @param throwError true - генерировать ошибки, если будут. При значении false -
     *                   ошибки просто выводятся в log
     */
    public void recoverGenIds(List<String> genIdNames, boolean throwError) throws Exception {
        GenIdService svc = getModel().bean(GenIdService.class);

        if (genIdNames == null) {
            genIdNames = new ArrayList<>();
            for (GenId g : svc.getGenIds()) {
                genIdNames.add(g.getName());
            }
        }

        //
        if (genIdNames.size() == 0) {
            return;
        }

        log.info("start recoverGenId");

        StringBuilder errors = new StringBuilder();

        Mdb mdb = getModel().createMdb();
        mdb.connect();
        try {

            // физические таблицы в базе данных
            log.info("grab database struct");
            NamedList<DbMetadataTable> tbls = mdb.getDbSource().bean(DbMetadataService.class).loadTables();

            for (String nm : genIdNames) {
                GenId g = svc.getGenIds().find(nm);
                if (g == null) {
                    continue; // нет генератора
                }
                if (!g.getDriver().isSupportUpdateCurrentId(g)) {
                    continue; // не поддерживает обновление значения
                }
                DbMetadataTable tb = tbls.find(g.getName());
                if (tb == null) {
                    continue; // нет физической таблицы
                }
                DbMetadataField idf = tb.getFields().find("id");
                if (idf == null) {
                    continue; // нет поля id
                }
                if (!VariantDataType.isNumber(idf.getDbDataType().getDataType())) {
                    continue; // не число
                }

                try {
                    // можно...

                    // берем текущее максимальное
                    long tbMax;
                    DbQuery q = mdb.openQuery("select max(id) from " + tb.getName());
                    try {
                        tbMax = q.getLong(0);
                    } finally {
                        q.close();
                    }

                    long genCur = g.getCurrentId();

                    if (tbMax > genCur) {
                        // обновляем на следующее
                        log.info("update genid {} for cur={}, max={}", g.getName(), genCur, tbMax);
                        svc.updateCurrentId(g.getName(), tbMax + g.getStep());
                    }

                } catch (Exception e) {
                    log.warn("Error for genid " + g.getName(), e);
                    errors.append(UtError.createErrorInfo(e).getText());
                    errors.append("\n");
                }
            }

        } finally {
            mdb.disconnect();
        }

        log.info("stop recoverGenId");

        if (throwError && errors.length() > 0) {
            throw new XError(errors.toString());
        }

    }

}
