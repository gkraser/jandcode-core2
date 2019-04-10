package jandcode.flexmark


import org.junit.jupiter.api.*

class Attr_Test extends CustomFlexmark_Test {

    @Test
    void test2() {
        parse("""
text1

Заголовок 1   { #id1 .red }
===========

текст { #rfv }

Заголовок 2    { id=123 attr1=333 #edc }
--------------

Заголовок 3   
--------------

текст {attr1=1}

""")
    }

    @Test
    void test3() {
        parse("""
Header 1 {#section1 .css-class-red attr1=value1}
========

Параграф {style="color:red"}

""")
    }


}
