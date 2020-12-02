<template>
    <tst-apx-panel class="framemanager2-stack-test-2fc050ba">
        <template #tools>
        </template>

        <jc-toolbar class="tools q-mb-md">
            <jc-action label="ПростоFrame1" @click="simpleFrame1"/>
            <jc-action label="ПростоFrame2" @click="simpleFrame2"/>
            <jc-action label="СтекFrame1" @click="simpleFrame1_stack"/>
            <jc-action label="СтекFrame2" @click="simpleFrame2_stack"/>
        </jc-toolbar>

        <div>
            <jc-shower-main-breadcrumbs/>
        </div>
        <div>jc-shower-main</div>

        <div class="shower">
            <jc-shower-main></jc-shower-main>
        </div>


    </tst-apx-panel>
</template>

<script>
import {apx} from '../vendor'
import Frame1 from './_frames/fm/Frame1'
import Frame2 from './_frames/fm/Frame2'

// apx.app.onAfterRun(() => {
//     apx.app.eventBus.$on("shower.main.change-frames", (ev) => {
//         console.info("FRAMES", ev);
//     })
// })

export default {
    extends: Vue.component('tst-apx-page'),
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

  .tools {
    border: 1px solid blue;
  }

  .shower {
    border: 1px solid green;
    padding: 10px;

    & > * {
      border: 3px solid #6a1b9a;
    }
  }

}

</style>
