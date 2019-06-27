package jandcode.commons;

import jandcode.commons.str.*;
import jandcode.commons.test.*;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UtString_Test extends Utils_Test {
    @Test
    public void testWrapLine() throws Exception {
        assertEquals(UtString.wrapLine(null, 20), "");
        assertEquals(UtString.wrapLine("", 20), "");
        assertEquals(UtString.wrapLine("hello", 20), "hello");
        assertEquals(UtString.wrapLine("hello", 4), "hell\no");
        assertEquals(UtString.wrapLine("he.llo", 4), "he.\nllo");
    }

    @Test
    public void testCapFirst() throws Exception {
        assertEquals(UtString.capFirst(null), "");
        assertEquals(UtString.capFirst(""), "");
        assertEquals(UtString.capFirst("hello"), "Hello");
        assertEquals(UtString.capFirst("h"), "H");
    }

    @Test
    public void testUncapFirst() throws Exception {
        assertEquals(UtString.uncapFirst(null), "");
        assertEquals(UtString.uncapFirst(""), "");
        assertEquals(UtString.uncapFirst("Hello"), "hello");
        assertEquals(UtString.uncapFirst("H"), "h");
    }

    @Test
    public void testXmlEscape() throws Exception {
        assertEquals(UtString.xmlEscape(null), "");
        assertEquals(UtString.xmlEscape(""), "");
        assertEquals(UtString.xmlEscape("&<>"), "&amp;&lt;&gt;");

    }

    @Test
    public void testRemoveFirstEmptyLine() throws Exception {
        assertEquals(UtString.removeFirstEmptyLine(null), "");
        assertEquals(UtString.removeFirstEmptyLine(""), "");
        assertEquals(UtString.removeFirstEmptyLine("a"), "a");
        assertEquals(UtString.removeFirstEmptyLine("  a  "), "  a  ");
        assertEquals(UtString.removeFirstEmptyLine("\n  a  "), "  a  ");
        assertEquals(UtString.removeFirstEmptyLine("\n  a  "), "  a  ");
        assertEquals(UtString.removeFirstEmptyLine(" \n  a  "), "  a  ");
        assertEquals(UtString.removeFirstEmptyLine(" \n \n  a  "), " \n  a  ");
    }

    @Test
    public void testIsWhite() throws Exception {
        assertEquals(UtString.isWhite(null), true);
        assertEquals(UtString.isWhite(""), true);
        assertEquals(UtString.isWhite(" \n\r\t "), true);
        assertEquals(UtString.isWhite(" d"), false);
    }

    @Test
    public void testTrimLast() throws Exception {
        assertEquals(UtString.trimLast(null), "");
        assertEquals(UtString.trimLast(""), "");
        assertEquals(UtString.trimLast("a"), "a");
        assertEquals(UtString.trimLast("aa"), "aa");
        assertEquals(UtString.trimLast("aa "), "aa");
        assertEquals(UtString.trimLast("a a\n\n  "), "a a");
    }

    @Test
    public void testToHexString() throws Exception {
        byte b[] = new byte[3];
        b[0] = (byte) 0xff;
        b[1] = (byte) 0xcc;
        b[2] = (byte) 0x1a;

        assertEquals(UtString.toHexString(b), "FFCC1A");
        assertEquals(UtString.toHexString(b, "-"), "FF-CC-1A");
    }

    @Test
    public void testSplitManySpaces() throws Exception {
        String s = "insert   into      \ntab1";
        String[] ar = s.split("\\s+");
        assertEquals(ar.length, 3);
        assertEquals(ar[2], "tab1");
    }

    @Test
    public void testTabToSpaces() throws Exception {
        assertEquals(UtString.tabToSpaces(null, 8), "");
        assertEquals(UtString.tabToSpaces("", 8), "");
        assertEquals(UtString.tabToSpaces("123", 8), "123");
        assertEquals(UtString.tabToSpaces("\t", 4) + "|", "    " + "|");
        assertEquals(UtString.tabToSpaces("\t\t", 4) + "|", "        " + "|");
        assertEquals(UtString.tabToSpaces("n\t1", 4), "n   1");
        assertEquals(UtString.tabToSpaces("nn\t1", 4), "nn  1");
        assertEquals(UtString.tabToSpaces("nnn\t1", 4), "nnn 1");
        assertEquals(UtString.tabToSpaces("nnnn\t1", 4), "nnnn    1");
        assertEquals(UtString.tabToSpaces("\tnn\t1\nn\t1", 4), "    nn  1\n" +
                "n   1");
    }

    @Test
    public void testNormalizeIndent() throws Exception {
        assertEquals(UtString.normalizeIndent(null), "");
        assertEquals(UtString.normalizeIndent(""), "");
        assertEquals(UtString.normalizeIndent("    "), "");
        assertEquals(UtString.normalizeIndent(" \n \n   \n  \t "), "");

        assertEquals(UtString.normalizeIndent(
                "    n   1  \n" +
                        "      nn  1   \n" +
                        "        nn  n 1\n" +
                        "    nnnn    1   \n" +
                        "    \n"),
                "n   1\n" +
                        "  nn  1\n" +
                        "    nn  n 1\n" +
                        "nnnn    1");

        assertEquals(UtString.normalizeIndent("p\n" +
                        " c \n" +
                        "n"),
                "p\n" +
                        " c\n" +
                        "n");

        assertEquals(UtString.normalizeIndent(" a\n\n\n  b\n\n"), "a\n" +
                "\n" +
                "\n" +
                " b");
    }

    @Test
    public void testIsDelimitedIntersect() throws Exception {
        assertEquals(UtString.isDelimitedIntersect("", "", ",", true), true);
        assertEquals(UtString.isDelimitedIntersect("", "", ",", false), false);

        assertEquals(UtString.isDelimitedIntersect("ins", "", ",", false), false);
        assertEquals(UtString.isDelimitedIntersect("ins", "", ",", true), true);
        assertEquals(UtString.isDelimitedIntersect("", "ins", ",", true), true);
        assertEquals(UtString.isDelimitedIntersect("ins", "ins", ",", true), true);
        assertEquals(UtString.isDelimitedIntersect("ins", "ins1", ",", true), false);
        assertEquals(UtString.isDelimitedIntersect("ins", "upd,del,ins", ",", true), true);
        assertEquals(UtString.isDelimitedIntersect("upd,ins", "upd,del,ins", ",", true), true);
        assertEquals(UtString.isDelimitedIntersect("ins", "upd,del,-ins", ",", true), false);
        assertEquals(UtString.isDelimitedIntersect("upd", "*,-ins", ",", true), true);
        assertEquals(UtString.isDelimitedIntersect("upd,del", "*,-ins", ",", true), true);
        assertEquals(UtString.isDelimitedIntersect("upd,ins", "*,-ins", ",", true), false);
    }

    @Test
    public void test_unCamelCase() throws Exception {
        assertEquals(UtString.unCamelCase(null), "");
        assertEquals(UtString.unCamelCase(""), "");
        assertEquals(UtString.unCamelCase("testIsDelimitedIntersect"), "test_is_delimited_intersect");
        assertEquals(UtString.unCamelCase("TestIsDelimitedIntersect"), "test_is_delimited_intersect");
        assertEquals(UtString.unCamelCase("folder/FileName"), "folder/file_name");
        assertEquals(UtString.unCamelCase("ABC"), "a_b_c");
        assertEquals(UtString.unCamelCase("ABC/CD"), "a_b_c/c_d");

    }

    @Test
    public void testRemovePrefix() throws Exception {
        assertEquals(UtString.removePrefix(null, null), null);
        assertEquals(UtString.removePrefix("", ""), null);
        assertEquals(UtString.removePrefix("AbcDe", "Ab"), "cDe");
        assertEquals(UtString.removePrefix("AbcDe", "Abe"), null);
        assertEquals(UtString.removePrefix("Abc", "Abc"), "");
    }

    @Test
    public void testRemoveSuffix() throws Exception {
        assertEquals(UtString.removeSuffix(null, null), null);
        assertEquals(UtString.removeSuffix("", ""), null);
        assertEquals(UtString.removeSuffix("AbcDe", "De"), "Abc");
        assertEquals(UtString.removeSuffix("AbcDe", "Dee"), null);
        assertEquals(UtString.removeSuffix("ADe", "De"), "A");
        assertEquals(UtString.removeSuffix("De", "De"), "");
    }

    @Test
    public void testRepeat() throws Exception {
        assertEquals(UtString.repeat(null, 10), "");
        assertEquals(UtString.repeat("", 10), "");
        assertEquals(UtString.repeat("1", 3), "111");
        assertEquals(UtString.repeat("12", 3), "121212");
        assertEquals(UtString.repeat("123", 3), "123123123");
    }

    @Test
    public void testPadRight() throws Exception {
        assertEquals(UtString.padRight(null, 3), "   ");
        assertEquals(UtString.padRight("", 3), "   ");
        assertEquals(UtString.padRight("a", 3), "a  ");
        assertEquals(UtString.padRight("a", 3, '*'), "a**");
        assertEquals(UtString.padRight("a", 5, "*|"), "a*|*|");
        assertEquals(UtString.padRight("a", 6, "*|"), "a*|*|*");
    }

    @Test
    public void testPadLeft() throws Exception {
        assertEquals(UtString.padLeft(null, 3), "   ");
        assertEquals(UtString.padLeft("", 3), "   ");
        assertEquals(UtString.padLeft("a", 3), "  a");
        assertEquals(UtString.padLeft("a", 3, '*'), "**a");
        assertEquals(UtString.padLeft("a", 5, "*|"), "*|*|a");
        assertEquals(UtString.padLeft("a", 6, "*|"), "*|*|*a");
    }

    @Test
    public void testPadCenter() throws Exception {
        assertEquals(UtString.padCenter(null, 3), "   ");
        assertEquals(UtString.padCenter("", 3), "   ");
        assertEquals(UtString.padCenter("a", 3), " a ");
        assertEquals(UtString.padCenter("a", 3, '*'), "*a*");
        assertEquals(UtString.padCenter("a", 5, "*|"), "*|a*|");
        assertEquals(UtString.padCenter("a", 6, "*|"), "*|a*|*");
    }

    @Test
    public void test_camelCase() throws Exception {
        assertEquals(UtString.camelCase(null), "");
        assertEquals(UtString.camelCase(""), "");
        assertEquals(UtString.camelCase("AbCd"), "AbCd");
        assertEquals(UtString.camelCase("table@sys/field@id"), "TableSysFieldId");
        assertEquals(UtString.camelCase("public void test_camelCase() throws Exception"), "PublicVoidTestCamelCaseThrowsException");
    }

    @Test
    public void testToList() throws Exception {
        assertEquals(UtString.toList(null).toString(), "[]");
        assertEquals(UtString.toList("").size(), 0);
        assertEquals(UtString.toList("").toString(), "[]");
        assertEquals(UtString.toList("1").toString(), "[1]");
        assertEquals(UtString.toList(",1, ,,  3, 55,").toString(), "[1, 3, 55]");
        ArrayList zz = new ArrayList();
        zz.add(678);
        zz.add("555");
        assertEquals(UtString.toList(zz).toString(), "[678, 555]");
    }

    @Test
    public void testCrc32Str() throws Exception {
        assertEquals(UtString.crc32Str("123"), "884863d2");
        assertEquals(UtString.crc32Str("124"), "162cf671");
        assertEquals(UtString.crc32Str(null), "00000000");
        assertEquals(UtString.padLeft("123", 8, "0"), "00000123");
    }

    @Test
    public void testJoin() throws Exception {
        assertEquals(UtString.join(null, "a"), "");
        ArrayList lst = new ArrayList();
        lst.add(1);
        assertEquals(UtString.join(lst, ":"), "1");
        lst.add(2);
        assertEquals(UtString.join(lst, ":"), "1:2");
        lst.add(true);
        assertEquals(UtString.join(lst, ":"), "1:2:true");
    }

    @Test
    public void testMd5Str() throws Exception {
        assertEquals(UtString.md5Str("111"), "698D51A19D8A121CE581499D7B701668");
        assertEquals(UtString.md5Str("111222"), "00B7691D86D96AEBD21DD9E138F90840");
        assertEquals(UtString.md5Str((String) null), "D41D8CD98F00B204E9800998ECF8427E");
        //
        assertEquals(UtString.md5Str(Arrays.asList("111")), "698D51A19D8A121CE581499D7B701668");
        assertEquals(UtString.md5Str(Arrays.asList("111", "222")), "00B7691D86D96AEBD21DD9E138F90840");
    }

    @Test
    public void testGetLine() throws Exception {
        assertEquals(UtString.getLine(null, 10), "");
        assertEquals(UtString.getLine("", 0), "");
        assertEquals(UtString.getLine("111", 0), "111");
        assertEquals(UtString.getLine("111", 2), "");
        assertEquals(UtString.getLine("111\n222\n333", 2), "333");
        assertEquals(UtString.getLine("111\n222\n333", 3), "");
    }

    private void base64check(String plainText, String base64Text) {
        String s1 = UtString.encodeBase64(plainText.getBytes());
        String s2 = new String(UtString.decodeBase64(base64Text));
        assertEquals(s1, base64Text);
        assertEquals(s2, plainText);
    }

    @Test
    public void testBase64() throws Exception {
        base64check("Aladdin:open sesame", "QWxhZGRpbjpvcGVuIHNlc2FtZQ==");
        base64check("", "");
        base64check("1", "MQ==");
        base64check("22", "MjI=");
        base64check("333", "MzMz");
        base64check("4444", "NDQ0NA==");
        base64check("55555", "NTU1NTU=");
        base64check("abc:def", "YWJjOmRlZg==");

        String s = UtString.encodeBase64(
                "111111111111111111111111111111111111111111111111111111111111111111".getBytes());
        assertEquals(s, "MTExMTExMTExMTExMTExMTExMTExMTExMTExMTExMTExMTExMTExMTExMTExMTExMTExMTExMTEx\r\n" +
                "MTExMTExMTEx");

        s = UtString.encodeBase64(null);
        assertEquals(s, "");
        s = UtString.encodeBase64(new byte[0]);
        assertEquals(s, "");

        byte[] ar = UtString.decodeBase64(null);
        assertEquals(ar.length, 0);
        ar = UtString.decodeBase64("");
        assertEquals(ar.length, 0);

    }

    //////

    @Test
    public void test_substVar1() throws Exception {
        ISubstVar h = new ISubstVar() {
            public String onSubstVar(String v) {
                return v.toUpperCase();
            }
        };
        //
        assertEquals(UtString.substVar("hello ${world}", h), "hello WORLD");
    }

    @Test
    public void test_indent() throws Exception {
        assertEquals(UtString.indent(null, "**"), "**");
        assertEquals(UtString.indent("a\nb", "**"), "**a\n**b");
        assertEquals(UtString.indent("a\n\nb", "**"), "**a\n**\n**b");
        assertEquals(UtString.indent("a\n\nb\n", "**"), "**a\n**\n**b");
    }

    @Test
    public void test_lineNum() throws Exception {
        assertEquals(UtString.lineNum(null, 100), 0);
        assertEquals(UtString.lineNum("", 100), 0);
        assertEquals(UtString.lineNum("\n", 100), 0);
        assertEquals(UtString.lineNum("1\n2222\n\3", 4), 1);
    }

}
