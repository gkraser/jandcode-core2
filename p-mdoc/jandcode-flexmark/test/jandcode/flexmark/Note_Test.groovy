package jandcode.flexmark


import org.junit.jupiter.api.*

class Note_Test extends CustomFlexmark_Test {

    @Test
    void test2() {
        parse("""
text1

!!! error
    hello1
    hello2

@@prop1

!!! error
    hello11
    hello21
    
""")
    }


}
