package jandcode.core.dao.data;

import jandcode.core.dao.*;

import java.util.*;

public class ContextIniterDao extends BaseDao {

    public void m1() throws Exception {
        Map<String, Object> m = (Map<String, Object>) getContext().bean("map1");
        m.put("k1", "v1");
        Map<String, Object> map2 = new HashMap<>();
        map2.put("k2", "v2");
        getContext().getBeanFactory().registerBean("map2", map2);
    }

}
