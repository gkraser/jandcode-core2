package jandcode.commons.groovy

import jandcode.commons.*
import jandcode.commons.groovy.impl.*
import jandcode.commons.test.*
import org.junit.jupiter.api.*

import static org.junit.jupiter.api.Assertions.*

public class GspParserTest extends Utils_Test {

    private void out3st(String s1, String s2, String s3) {
        if (s1 == null) s1 = "";
        if (s2 == null) s2 = "";
        if (s3 == null) s3 = "";
        //
        String[] a1 = s1.split("\n");
        String[] a2 = s2.split("\n");
        String[] a3 = s3.split("\n");

        for (int i = 0; i < a1.length; i++) {
            a1[i] = UtString.trimLast(a1[i]);
        }

        for (int i = 0; i < a2.length; i++) {
            a2[i] = UtString.trimLast(a2[i]);
        }

        for (int i = 0; i < a3.length; i++) {
            a3[i] = UtString.trimLast(a3[i]);
        }

        //
        int mxCnt = Math.max(a1.length, a2.length);
        mxCnt = Math.max(mxCnt, a3.length);
        //
        int maxlen1 = 0;
        for (String s : a1) {
            if (s.length() > maxlen1) {
                maxlen1 = s.length();
            }
        }
        int maxlen2 = 0;
        for (String s : a2) {
            if (s.length() > maxlen2) {
                maxlen2 = s.length();
            }
        }

        //
        for (int i = 0; i < mxCnt; i++) {
            String rs1 = "";
            if (i < a1.length) rs1 = a1[i];
            String rs2 = "";
            if (i < a2.length) rs2 = a2[i];
            String rs3 = "";
            if (i < a3.length) rs3 = a3[i];
            //
            String rs = UtString.padRight(rs1, maxlen1, ' ') + "|" +
                    UtString.padRight(rs2, maxlen2, ' ') + "|" +
                    rs3;
            System.out.println(rs);
        }

    }

    private void runTst(String src, String res, boolean doNormalize, boolean doCompile = true) throws Exception {
        if (doNormalize) {
            src = UtString.normalizeIndent(src);
            res = UtString.normalizeIndent(res);
        }
        //
        GspParser tp = new GspParser();
        tp.setPrintMethodName("print")
        UtLoad.fromString(tp, src);
        String resReal = UtString.normalizeIndent(tp.getScriptText());
        try {
            assertEquals(res.trim(), resReal.trim());
            String[] r1 = resReal.split("\n");
            String[] r2 = src.split("\n");
            assertEquals(r1.length, r2.length, "Количество строк");
        } catch (Error e) {
            System.out.println("== [" + testName + "] =================================================================");
            out3st(src, resReal, res);
            System.out.println("=================================================================================");
            System.out.println("---------------------- [res real]");
            System.out.println(resReal);
            System.out.println("---------------------- [res]");
            System.out.println(res);
            System.out.println("---------------------- [src] ");
            System.out.println(src);
            System.out.println("=================================================================================");
            throw e;
        }
        //
        if (doCompile) {
            checkCompile(tp.getScriptText())
        }
    }

    GroovyCompiler compiler;

    private checkCompile(String t) {
        if (compiler == null) {
            compiler = UtGroovy.createCompiler();
        }
        try {
            GroovyClazz z = compiler.getClazz(SimpleGspTemplate.class, "void doGenerate()", t, false)
        } catch (e) {
            println "ERROR COMPILE TEXT ------\n${t}\n------\n"
            throw e
        }
    }

    private void runTst(String src, String res) throws Exception {
        runTst(src, res, true)
    }

    @Test
    public void testLastEmpty() throws Exception {
        GspParser tp = new GspParser();
        tp.setPrintMethodName("print")
        UtLoad.fromString(tp, "a\n\n")
        String s = UtString.normalizeIndent(tp.getScriptText());
        assertEquals(s, ";;print(\"a\\n\");\n" +
                ";;print(\"\\n\");");
    }

    @Test
    public void test1() throws Exception {
        runTst('''
s1
  s2
s3
<% c1() %>
<%
   c2()
%>
s4
   <% c3() %>
s5
s6 <% c4(); %>
   <% c5(); %> s7
s8 <% c6(); %> s9
<%= v1 %>
s1 <%= v1 %>
<%= v1 %> s1
s1 <%= v1 %> s2
''', '''
;;print("s1\\n");
;;print("  s2\\n");
;;print("s3\\n");
 c1()

   c2()

;;print("s4\\n");
 c3()
;;print("s5\\n");
;;print("s6 "); c4(); ;;print("\\n");
 c5(); ;;print(" s7\\n");
;;print("s8 "); c6(); ;;print(" s9\\n");
;;print( v1 );;;print("\\n");
;;print("s1 ");;;print( v1 );;;print("\\n");
;;print( v1 );;;print(" s1\\n");
;;print("s1 ");;;print( v1 );;;print(" s2");
''');
    }

    @Test
    public void testEmpty() throws Exception {
        runTst('''
''', '''
''');
    }

    @Test
    public void spacesAfterAutoprint() throws Exception {
        runTst('''
s1 <%= v1 %>
s2 <%= v2 %>
''', '''
;;print("s1 ");;;print( v1 );;;print("\\n");
;;print("s2 ");;;print( v2 );
''');
    }

    @Test
    public void spacesAfterInlinecode() throws Exception {
        runTst('''
s1 <% v1(); %>
z
''', '''
;;print("s1 "); v1(); ;;print("\\n");
;;print("z");
''');
    }

    @Test
    public void noSpacesInInlinecode() throws Exception {
        runTst('''
z1
   <% c3() %>
z2
''', '''
;;print("z1\\n");
 c3()
;;print("z2");
''');
    }

    @Test
    public void emptyString() throws Exception {
        runTst('''
z1

z2
''', '''
;;print("z1\\n");
;;print("\\n");
;;print("z2");
''');
    }

    @Test
    public void twoCodeSpaces() throws Exception {
        runTst('''
a
<% c1() %>

b

<%
   c2 %>
c
''', '''
;;print("a\\n");
 c1()
;;print("\\n");
;;print("b\\n");
;;print("\\n");

   c2
;;print("c");
''');
    }

    @Test
    public void textOnly() throws Exception {
        runTst('''
z1
z2
''', '''
;;print("z1\\n");
;;print("z2");
''');
    }

    @Test
    public void twoCodeInStr() throws Exception {
        runTst('''
z1 <% c1(); %> <% c2(); %>
<% c1(); %> <% c2(); %>
<% c1(); %> <% c2(); %> z2
<% c1(); %> z2 <% c2(); %>
''', '''
;;print("z1 "); c1(); ;;print(" "); c2(); ;;print("\\n");
 c1();  c2();
 c1();  c2(); ;;print(" z2\\n");
 c1(); ;;print(" z2 "); c2();
''');
    }

    @Test
    public void expand1() throws Exception {
        runTst('''
z1 \${c1} z2
\${c1}
z1 \${c1}
\${c1} z2
\${c1}\${c2} \${c2}                                                
''', '''
;;print("z1 ");;;print(c1);;;print(" z2\\n");
;;print(c1);;;print("\\n");
;;print("z1 ");;;print(c1);;;print("\\n");
;;print(c1);;;print(" z2\\n");
;;print(c1);;;print(c2);;;print(" ");;;print(c2);
''');
    }

    @Test
    public void rem1() throws Exception {
        runTst('''
z1
<%- code() --%>
<%-- rem --%> \${1}
 \${2}   <%--
    /*rem*/
    --%>
z2
''', '''
;;print("z1\\n");
- code() --
/* rem */;;print(" ");;;print(1);;;print("\\n");
;;print(" ");;;print(2);;;print("   ");/*
    /\\*\\rem\\*\\/
    */;;print("\\n");
;;print("z2");
''');
    }

    @Test
    public void rem2() throws Exception {
        runTst('''
z1
<%- code() --%>
%{-- rem --}% \${1}
 \${2}   %{--
    /*rem*/
    --}%
z2
''', '''
;;print("z1\\n");
- code() --
/* rem */;;print(" ");;;print(1);;;print("\\n");
;;print(" ");;;print(2);;;print("   ");/*
    /\\*\\rem\\*\\/
    */;;print("\\n");
;;print("z2");
''');
    }

    @Test
    public void import1() throws Exception {
        runTst('''
<%@ page import="a; b" %>
<%@ page import="x" %> \${b}
z
''', '''
import a; import b;
import x; ;;print(" ");;;print(b);;;print("\\n");
;;print("z");
''', true, false);
    }

    @Test
    public void tags1() throws Exception {
        runTst('''
<xc:td/>
<xc:td
    />
<xc:td a="1" bb="xc" />
<xc:td a1="1"
        bb1=""
         cc="jcd"/>
z
''', '''
outTag('xc/td');;;print("\\n");
outTag('xc/td'
    );;;print("\\n");
outTag('xc/td' ,a:"1" ,bb:"xc" );;;print("\\n");
outTag('xc/td' ,a1:"1"
        ,bb1:""
         ,cc:"jcd");;;print("\\n");
;;print("z");
''');
    }

    @Test
    public void tags2() throws Exception {
        runTst('''
<xc:tr>
    x
</xc:tr>
''', '''
outTag('xc/tr') {
;;print("    x\\n");
}
''');
    }

    @Test
    public void tags3() throws Exception {
        runTst('''
<xc:tr a="1"
         b="3">
    <xc:tr>
        x
    </xc:tr>
a </xc:tr> b
''', '''
outTag('xc/tr' ,a:"1"
         ,b:"3") {
;;print("    ");outTag('xc/tr') {;;print("\\n");
;;print("        x\\n");
}
;;print("a ");};;print(" b");
''');
    }

    @Test
    public void tags4() throws Exception {
        runTst('''
  <xc:tr a="${3}" b="3" c="a-${b}" d="${a+1}"/>
  ''', '''
outTag('xc/tr' ,a:3 ,b:"3" ,c:"a-${b}" ,d:a+1);
''');
    }

    @Test
    public void dollar1() throws Exception {
        runTst('''
q \$ w
q\$w
\$q
q\$
\$(a)
\${q}
<a\${q.z}b/>
''', '''
;;print("q \\\$ w\\n");
;;print("q\\\$w\\n");
;;print("\\\$q\\n");
;;print("q\\\$\\n");
;;print("\\\$(a)\\n");
;;print(q);;;print("\\n");
;;print("<a");;;print(q.z);;;print("b/>");
''');
    }

    @Test
    public void lastTZ1() throws Exception {
        runTst('''
<a><% outTml("jc/cell", f: f) %></td>
  ''', '''
;;print("<a>"); outTml("jc/cell", f: f) ;;print("</td>");
''');
    }

    @Test
    public void test_lastEOL_no() throws Exception {
        runTst('''a
b
c''', ''';;print("a\\n");
;;print("b\\n");
;;print("c");
''', false);

    }

    @Test
    public void test_lastEOL_yes() throws Exception {
        runTst('''a
b

c
''', ''';;print("a\\n");
;;print("b\\n");
;;print("\\n");
;;print("c\\n");''', false);

    }

    @Test
    public void minusInName() throws Exception {
        runTst('''
<q-w:e-r/><q--w:e--r a="0" a-b="1" c--d--e="2"/>
  ''', '''
outTag('q-w/e-r');outTag('q--w/e--r' ,a:"0" ,"a-b":"1" ,"c--d--e":"2");
''');

    }

    @Test
    public void stripCommentNewLine() throws Exception {
        runTst('''
z1
<%- code() --%>
  %{-- rem --}%
 \\${2}   %{--
    rem
    --}%
zz
 %{--
    rem
    --}%
z2
  ''', '''
;;print("z1\\n");
- code() --
/* rem */
;;print(" \\\\");;;print(2);;;print("   ");/*
    rem
    */;;print("\\n");
;;print("zz\\n");
/*
    rem
    */
;;print("z2");
''');

    }

    @Test
    public void test_indentLost_in_endTag() throws Exception {
        runTst('''<a:b>
  <a>
  </a>
</a:b>
''', '''outTag('a/b') {
;;print("  <a>\\n");
;;print("  </a>\\n");
}''', false);

    }

}
