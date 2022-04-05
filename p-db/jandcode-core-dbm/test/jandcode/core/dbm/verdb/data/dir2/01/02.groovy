import jandcode.core.dbm.mdb.*

class t02 {
    void exec(Mdb mdb) {
        mdb.execScript("""
create table tab2
(
    id varchar(10)
) 
~~
alter table tab2
    add column f2 varchar(10)
""")
    }
}