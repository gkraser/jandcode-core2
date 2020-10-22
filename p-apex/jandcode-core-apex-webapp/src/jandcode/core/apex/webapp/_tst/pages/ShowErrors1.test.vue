<template>
    <tst-apex-panel class="showerrors1-ee76c43d">
        <div class="row q-gutter-sm">
            <jc-btn label="throw new Error('msg')"
                    @click="throwError1"/>
            <jc-btn label="Vue template error"
                    @click="errorInTemplate1"/>
            <jc-btn label="axios error"
                    @click="errorAxios1"/>
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
    </tst-apex-panel>
</template>

<script>
import axios from 'axios'

export default {
    extends: Vue.component('tst-apex-page'),
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
