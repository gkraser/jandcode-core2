package jandcode.mdoc

import jandcode.mdoc.cm.snippet.*
import org.junit.jupiter.api.*

class VueSnippet_Test extends CustomMDoc_Test {

    String vue1 = """
<template>
    <div>
        Страница
        <!-- = xpart1 -->
        <span>11</span>
        <!-- = xpart2 -->
        <span>22</span>
        <!-- = -->        
    </div>
</template>

<script>
import {apx} from '../vendor'

export default {
    extends: apx.JcFrame,
    props: {},
    //= part0
    created() {
        //= part1
        this.title = 'Пустая страница'
        //= 
    },
    frameInit() {
    },
    data() {
        //= part2
        return {}
    },
    methods: {},
    //= 
}
</script>

<style lang="less">
   .div {
      color: 123;
   }
</style>
"""

    @Test
    public void test1() throws Exception {
        VueSnippet z = new VueSnippet()
        z.configure(vue1, "vue")
        //
        for (p in z.getParts()) {
            utils.delim("${p.getName()} (${p.getLang()})")
            println p.getText()
        }
    }


}
