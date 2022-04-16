package jandcode.core.dbm.fixture

import groovy.transform.*
import jandcode.commons.stopwatch.*
import jandcode.core.db.*
import jandcode.core.dbm.genid.std.*
import jandcode.core.dbm.mdb.*
import jandcode.core.dbm.sql.*
import jandcode.core.store.*

/**
 * Утилиты для fixture в контексте mdb
 */
@CompileStatic
class FixtureMdbUtils extends BaseMdbUtils {

    class Progress {

        private int step = 1000
        private int cur = 0
        private int count = 0
        private StopwatchSet stopwatch = new DefaultStopwatchSet(true)
        private String title = ""

        void start(String title, int count) {
            this.count = count
            this.cur = 0
            this.title = title
            String suff = ""
            if (count > 0) {
                suff = ": ${count}"
            }
            println this.title + suff
            stopwatch.start(this.title)
        }

        void step() {
            this.cur++
            if (this.count > 0 && this.cur % this.step == 0) {
//todo в консоле idea не выводит
// print("${this.cur}/${this.count}\r")
// System.out.flush()
            }
        }

        void stop() {
            print("\r")
            if (this.count > 0) {
                stopwatch.stop(this.title, this.count)
            } else {
                stopwatch.stop(this.title)
            }
        }
    }

    FixtureMdbUtils(Mdb mdb) {
        super(mdb)
    }

    /**
     * Очистить данные в таблице
     *
     * @param table в какой таблице
     * @param startId с какой id
     * @param endId по какую id
     */
    void clean(String table, long startId, long endId) throws Exception {
        mdb.execQuery("delete from ${table} where id>=:startId and id<=:endId",
                [startId: startId, endId: endId])
    }

    /**
     * Очистить данные для фикстуры.
     * Будут удалены все записи в диапазоне startId-endID
     * для всех таблиц в фикстуре.
     *
     * @param fx фикстура
     * @param showProgress показывать ли progress
     */
    void cleanFixture(Fixture fx, boolean showProgress) throws Exception {
        mdb.startTran()
        try {
            Progress prg = null
            if (showProgress) {
                prg = new Progress()
                prg.stopwatch.start("clean fixture")
            }

            // удаляем в обратном записи порядке
            for (int i = fx.getTables().size() - 1; i >= 0; i--) {
                FixtureTable t = fx.getTables().get(i)
                FixtureRangeId rangeId = t.getRangeId()

                if (prg != null) {
                    prg.start(t.name + " clean", 0)
                }

                clean(t.getName(), rangeId.getStartId(), rangeId.getEndId())

                if (prg != null) {
                    prg.stop()
                }

            }

            mdb.commit()

            if (prg != null) {
                prg.stopwatch.stop("clean fixture")
            }

        } catch (Exception e) {
            mdb.rollback(e)
        }
    }

    /**
     * Записать фикстуру.
     *
     * @param fx фикстура
     * @param showProgress показывать ли progress
     */
    void saveFixture(Fixture fx, boolean showProgress) throws Exception {
        mdb.startTran()
        try {
            Progress prg = null
            if (showProgress) {
                prg = new Progress()
                prg.stopwatch.start("save fixture")
            }

            SqlBuilder sqlBuilder = mdb.createSqlBuilder()

            // записываем в порядке записи
            for (FixtureTable t : fx.getTables()) {
                String sql = sqlBuilder.makeSqlInsert(t.getName(), t.getStore())
                DbQuery q = mdb.createQuery(sql)
                //
                if (prg != null) {
                    prg.start(t.name + " save", t.store.size())
                }
                for (StoreRecord rec : t.getStore()) {
                    q.setParams(rec)
                    q.exec()
                    if (prg != null) {
                        prg.step()
                    }
                }
                if (prg != null) {
                    prg.stop()
                }
            }

            mdb.commit()

            if (prg != null) {
                prg.stopwatch.stop("save fixture")
            }

            if (prg != null) {
                prg.stopwatch.start("recover genId")
            }

            List<String> genIdNames = []
            for (t in fx.tables) {
                genIdNames.add(t.name)
            }
            GenIdTools genIdTools = new GenIdTools(fx.model)
            genIdTools.recoverGenIds(genIdNames, true)

            if (prg != null) {
                prg.stopwatch.stop("recover genId")
            }

        } catch (Exception e) {
            mdb.rollback(e)
        }
    }

    /**
     * Обновить фикстуру. Сначала выполняется cleanFixture, потом saveFixture.
     *
     * @param fx фикстура
     * @param showProgress показывать ли progress
     */
    void updateFixture(Fixture fx, boolean showProgress) throws Exception {
        cleanFixture(fx, showProgress)
        saveFixture(fx, showProgress)
    }

}
