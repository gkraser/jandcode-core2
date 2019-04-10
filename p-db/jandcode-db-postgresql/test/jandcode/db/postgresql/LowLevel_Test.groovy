package jandcode.db.postgresql

import jandcode.core.test.*
import jandcode.commons.*
import org.junit.jupiter.api.*

import java.sql.*

//@Ignore
class LowLevel_Test extends App_Test {

    @Test
    public void test1() throws Exception {
        UtClass.getClass("org.postgresql.Driver")
        Properties p = new Properties()
        p.put("user", "ks")
        p.put("password", "")
        //Connection con = DriverManager.getConnection("jdbc:postgresql://localhost/postgres", p) //, "ks", null)
        Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", p)
        //, "ks", null)
        PreparedStatement st = con.prepareStatement(
                "SELECT datname FROM pg_database",
        )
        ResultSet rs = st.executeQuery()
        while (rs.next()) {
            println "==" + rs.getString(1)
        }

    }

}
