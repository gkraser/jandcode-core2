package jandcode.core.apx.data


import jandcode.core.dbm.dao.*
import jandcode.core.store.*

class Store1_Dao extends BaseModelDao {

    Store storeWithDict() {
        Store st = mdb.createStore()
        st.addField("id", "long")
        st.addField("color", "long").setDict("color")
        st.add(id: 1, color: 1)
        st.add(id: 3, color: 3)
        return st
    }


}
