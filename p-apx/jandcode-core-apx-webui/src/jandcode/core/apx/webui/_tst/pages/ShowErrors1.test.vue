<template>
    <tst-apx-panel class="showerrors1-ee76c43d">
        <div class="row q-gutter-sm">
            <jc-btn label="throw new Error('msg')"
                    @click="throwError1"/>
            <jc-btn label="error in code"
                    @click="errorInCode1"/>
            <jc-btn label="Vue template error"
                    @click="errorInTemplate1"/>
            <jc-btn label="axios error"
                    @click="errorAxios1"/>
            <jc-btn label="ajax error"
                    @click="errorAjax1"/>
            <jc-btn label="promise error"
                    @click="errorPromise1"/>
            <jc-btn label="Jc.loadModule error"
                    @click="errorLoadModule1"/>
            <jc-btn label="fetch error"
                    @click="errorFetch1"/>
            <jc-btn label="fetch error 2"
                    @click="errorFetch2"/>
            <jc-btn label="Vue error in render"
                    @click="errorInRender1"/>
        </div>
    </tst-apx-panel>
</template>

<script>
import axios from 'axios'
import {apx, tst} from '../vendor'

export default {
    mixins: [tst.mixins.cfgStore],
    components: {},
    created() {
        this.cfgStore.applyDefault({})
    },
    data() {
        return {}
    },
    methods: {
        applyCfg() {
            let cfg = this.cfg
        },
        throwError1() {
            throw new Error('Ошибка возникла!')
        },
        errorInCode1() {
            let a = bbb / ccc
        },
        errorInTemplate1() {
            let vm = new Vue({
                template: '<div>{{missingProp1}/div>',
            })
            vm.$mount()
        },
        errorInRender1() {
            let vm = new Vue({
                template: '<div>{{missingProp}}</div>',
            })
            vm.$mount()
        },
        errorAxios1() {
            axios.get('xxx')
        },
        errorAjax1() {
            apx.jsaBase.ajax.request({
                url:'xxx'
            })
        },
        errorPromise1() {
            let p = new Promise(function(reslove) {
                console.info("start promise");
                throw new Error('Ошибка в Promise')
            })
        },
        errorFetch1() {
            fetch('http://1111.22.22.22')
        },
        errorFetch2() {
            fetch('xxx').then(res => {
                if (!res.ok) {
                    throw res
                }
            })
        },
        errorLoadModule1() {
            Jc.loadModule('xxx')
        },
    }
}
</script>

<style lang="less">

.showerrors1-ee76c43d {
}

</style>
