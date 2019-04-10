package jandcode.flexmark


import org.junit.jupiter.api.*

class Cm_Test extends CustomFlexmark_Test {

    @Test
    void cm1() {
        parse("""
text1

@@include file=./ssss lang=xml

@@include file=./ssss1 lang=xml

## head3 {}

* aqaz

  @@include file=..c

* sss

""")
    }

    @Test
    void cm2() {
        parse("""
text1

@@include **file**
 ~=./ssss
 lang=
 xml

text2
""")
    }

    @Test
    void cm3() {
        parse("""
text1

@@line1
line2
line3

text2

""")
    }

    @Test
    void cm4() {
        parse("""
text1

@@line1
line2 **dddd**
line3

text2

""")
    }


}
