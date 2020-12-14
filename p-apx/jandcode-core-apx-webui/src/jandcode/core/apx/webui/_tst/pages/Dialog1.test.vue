<template>
    <tst-apx-panel class="dialog1-test-f4712288">
        <template #tools>
        </template>

        <div>Диалоги. Отладка UI.</div>
        <jc-toolbar>
            <jc-action label="ToContent" @click="dialog('ToContent')"/>
            <jc-action label="Empty" @click="dialog('Empty')"/>
            <jc-action label="Panels" @click="dialog('Panels')"/>
            <jc-action label="BodyFit" @click="dialog('BodyFit')"/>
        </jc-toolbar>

    </tst-apx-panel>
</template>

<script>
import {apx, tst} from '../vendor'
import ToContent from './_frames/dialog/ToContent'

let base = 'jandcode/core/apx/webui/_tst/pages/_frames/dialog/'

export default {
    mixins: [tst.mixins.cfgStore],
    components: {
        ToContent
    },
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
        dialog(comp, params) {
            apx.showDialog({
                frame: base + comp + '.vue',
                params: params
            })
        }
    }
}
</script>

<style lang="less">

.dialog1-test-f4712288 {


}

</style>
