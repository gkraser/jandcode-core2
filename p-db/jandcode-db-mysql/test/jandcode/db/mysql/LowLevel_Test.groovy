package jandcode.db.mysql

import jandcode.core.test.*
import jandcode.commons.*
import org.junit.jupiter.api.*

import java.sql.*

//@Ignore
class LowLevel_Test extends App_Test {

    @Test
    public void test1() throws Exception {
        UtClass.getClass("jandcode.db.mysql.MysqlDbDriver")
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost/?useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8", "root", null)
        PreparedStatement st = con.prepareStatement(
                "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME=?",
        )
        st.setString(1, "jc2_db_test_create")
        ResultSet rs = st.executeQuery()
        while (rs.next()) {
            println "==" + rs.getString(1)
        }

    }

    @Test
    public void serverTime() throws Exception {
        UtClass.getClass("jandcode.db.mysql.MysqlDbDriver")
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost/?useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8", "root", null)
        PreparedStatement st = con.prepareStatement(
                "SELECT NOW()",
        )
        ResultSet rs = st.executeQuery()
        while (rs.next()) {
            println "==" + rs.getString(1)
        }

    }


}
