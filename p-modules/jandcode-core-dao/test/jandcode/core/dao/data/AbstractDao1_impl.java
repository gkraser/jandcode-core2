package jandcode.core.dao.data;

public class AbstractDao1_impl implements IDaoIntf1 {

    public static String RES = "";

    public String m1(String p1, int p2) {
        return "m1 from AbstractDao1_impl: " + p1 + "," + p2;
    }

    public String m2() {
        return "m2 from AbstractDao1_impl";
    }

    public void m3(long a, long b) {
        RES = "m3 from AbstractDao1_impl: " + a + "," + b;
    }

}
