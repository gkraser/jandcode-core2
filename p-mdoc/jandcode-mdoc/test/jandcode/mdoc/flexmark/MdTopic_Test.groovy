package jandcode.mdoc.flexmark


import org.junit.jupiter.api.*

class MdTopic_Test extends CustomFlexmark_Test {

    @Test
    void test1() {

        parseTopic("""
text1

Заголовок 1   { #id1 .red }
===========

текст { #rfv }

Заголовок 2    { id=123 attr1=333 #edc }
--------------

### int1

#### int1.2

### int2

Заголовок 3   
--------------

текст

""")
    }

    @Test
    void test2() {

        parseTopic("""\
---
key:value1
key:value21
list:
  - value 1
  - value 2
---

Заголовок 1
===========

текст
""")
    }

    @Test
    void headLevel1() {

        parseTopic("""\
---
title:value1
---

## 2
### 2
#### 2
### 2
# 2

""")
    }

    @Test
    void headLevel2() {

        parseTopic("""\
# title
# 3
## 2
### 2
#### 2
### 2
# 2

""")
    }


}
