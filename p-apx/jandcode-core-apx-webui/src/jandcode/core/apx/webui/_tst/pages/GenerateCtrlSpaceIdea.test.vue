<template>
    <tst-apx-panel class="generatectrlspaceidea-test-de486d22">
        <template #tools>
        </template>
        <h5>Генерация файла регистрации компонентов</h5>
        <small>что работал Ctrl+Space, поместите этот файл себе в проект в папку _tst и
            <b>НЕ ИМПОРТИРУЙТЕ</b> его</small>
        <h5>modules</h5>
        <div>{{ modules }}</div>
        <h5>_dummy_vue_reg.js</h5>
        <pre>
{{ textJs }}
        </pre>

    </tst-apx-panel>
</template>

<script>

async function compFromModules(moduleNames) {

    let imp = ''
    let body = ''

    let n = 0
    for (let mn of moduleNames) {
        await Jc.loadModule(mn)
        let comps = require(mn)
        n++
        let vn = 'x' + n
        imp += `import * as ${vn} from '${mn}'\n`

        for (let a in comps) {
            let z = comps[a]
            if (z.name) {
                body += `Vue.component('${z.name}', ${vn}.${a});\n`
            }
        }
    }

    return imp + "\n" + body
}


export default {
    extends: Vue.component('tst-apx-page'),
    components: {},
    created() {
        this.cfgStore.applyDefault({})
        //
        compFromModules(this.modules).then((a) => {
            this.textJs = a;
        })
    },
    data() {
        return {
            modules: [
                "jandcode/core/apx/webui/js/components/index.js",
                "jandcode/core/apx/tst/js/components/index.js",
            ],
            textJs: ''
        }
    },
    methods: {
        applyCfg() {
            let cfg = this.cfg
        },
    }
}
</script>

<style lang="less">

.generatectrlspaceidea-test-de486d22 {


}

</style>
