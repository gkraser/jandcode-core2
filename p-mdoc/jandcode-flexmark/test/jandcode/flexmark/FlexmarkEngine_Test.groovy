package jandcode.flexmark


import org.junit.jupiter.api.*

class FlexmarkEngine_Test extends CustomFlexmark_Test {

    @Test
    void test1() {
        parse("""
Hello!
""")
    }

    @Test
    void code() {
        parse("""
текст
```
code1
code2
```
Текст

```java
code1
code2
```

""")
    }

    @Test
    void table() {
        parse("""
текст

| Head 1 | Head 2|
| --- | --- |
| cell 1 | cell 2 |
| cell 3 | cell 4 |


""")
    }

    @Test
    void definition() {
        parse("""
текст

Определение 1
: Описание 1

Определение 2
: Описание 2

""")
    }

    @Test
    void head1() {
        parse("""
Заголовок 1
===========

текст

Заголовок 2
-----------

текст

Header 2
--------

текст

""")
    }

    @Test
    void codeAttrs() {
        parse("""
текст
```aaa bbb=ccc title="Приветы ура"
code1
code2
```

""")
    }

}
