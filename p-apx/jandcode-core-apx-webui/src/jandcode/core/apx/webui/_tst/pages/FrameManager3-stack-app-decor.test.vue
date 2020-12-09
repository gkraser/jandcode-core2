<template>
    <tst-apx-panel class="framemanager2-stack-test-2fc050ba" debug-bg no-padding>
        <template #tools>
        </template>

        <div class="wrap-app">
            <App container style="height: calc(100vh - 72px);" :own="this">
                <template #toolbar>
                    <jc-action label="ПростоFrame1" @click="simpleFrame1"/>
                    <jc-action label="ПростоFrame2" @click="simpleFrame2"/>
                    <jc-action label="СтекFrame1" @click="simpleFrame1_stack"/>
                    <jc-action label="СтекFrame2" @click="simpleFrame2_stack"/>
                </template>
            </App>
        </div>

    </tst-apx-panel>
</template>

<script>
import {apx, tst} from '../vendor'
import Frame1 from './_frames/fm/Frame1'
import Frame2 from './_frames/fm/Frame2'

export default {
    mixins: [tst.mixins.cfgStore, apx.JcApp],
    components: {
        App: apx.components.JcDecorAppStd,
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
        simpleFrame1() {
            apx.showFrame({
                frame: Frame1
            })
        },
        simpleFrame2() {
            apx.showFrame({
                frame: Frame2,
                params: {
                    pause: 500
                }
            })
        },
        simpleFrame1_stack() {
            apx.showFrame({
                frame: Frame1,
                stack: true,
            })
        },
        simpleFrame2_stack() {
            apx.showFrame({
                frame: Frame2,
                stack: true,
                params: {
                    pause: 500
                }
            })
        },
    }
}
</script>

<style lang="less">

.framemanager2-stack-test-2fc050ba {


}

</style>
