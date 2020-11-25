<template>
    <tst-apx-panel class="app2-test-89ebaa47" no-padding debug-bg>
        <template #tools>
        </template>

        <div class="wrap-app">
            <App container style="height: calc(100vh - 72px);" :own="this">
                <template #toolbar-right>
                    <jc-toolbar>
                        <jc-action label="home" @click="home"/>
                        <jc-action label="frame1" @click="frame1"/>
                        <jc-action label="frame2" @click="frame2"/>
                        <jc-action label="dialog1" @click="dialog1"/>
                        <jc-action label="/path1" @click="path1('/path1')"/>
                        <jc-action label="/path1/123"
                                   @click="path1('/path1/123',{p2:'P2Value'})"/>
                        <jc-action label="/fn - функция"
                                   @click="path1('/fn',{tag:'function',p2:'P2Value',p1:'pp11'})"/>
                        <jc-action label="/fnp - Promise"
                                   @click="path1('/fnp',{tag:'Promise', p2:'P2ValuePromise',p1:'pp11'})"/>
                        <jc-action label="/cls - vue-класс"
                                   @click="path1('/cls',{p2:'P442Value',p1:'pp141'})"/>
                    </jc-toolbar>
                </template>
                <template #main>
                    <q-page>
                        <jc-shower-main></jc-shower-main>
                    </q-page>
                </template>
            </App>
        </div>


    </tst-apx-panel>
</template>

<script>
import {apx, jsaBase} from '../vendor'
import Home from './_frames/fm/Home'
import Frame2 from './_frames/fm/Frame2'
import Dialog1 from './_frames/fm/Dialog1'

let Frame2Cls = Vue.extend(Frame2)

jsaBase.app.onBeforeRun(function() {
    let routes = [
        {
            path: '',
            frame: 'jandcode/core/apx/webui/_tst/pages/_frames/fm/Home.vue',
        },
        {
            path: '/path1/:idn?',
            frame: 'jandcode/core/apx/webui/_tst/pages/_frames/fm/Frame1.vue',
        },
        {
            path: '/fn',
            frame: function() {
                return 'jandcode/core/apx/webui/_tst/pages/_frames/fm/Frame1' + '.vue'
            }
        },
        {
            path: '/fnp',
            frame: async function() {
                return 'jandcode/core/apx/webui/_tst/pages/_frames/fm/Frame1' + '.vue'
            }
        },
        {
            path: '/cls',
            frame: Frame2Cls
        },

    ]
    jsaBase.app.frameRouter.addRoutes(routes)
})

export default {
    extends: Vue.component('tst-apx-page'),
    mixins: [apx.JcApp],

    components: {},

    created() {
        this.title = 'FrameManager1.test.vue'
        this.cfgStore.applyDefault({})
    },
    data() {
        return {}
    },
    methods: {
        applyCfg() {
            let cfg = this.cfg
        },
        home() {
            apx.showFrame({
                frame: Home
            })
        },
        frame1() {
            apx.showFrame({
                frame: 'jandcode/core/apx/webui/_tst/pages/_frames/fm/Frame1.vue'
            })
        },
        frame2() {
            apx.showFrame({
                frame: Frame2,
                params: {
                    cnt: 100
                }
            })
        },
        dialog1() {
            apx.showDialog({
                frame: Dialog1,
                onOk(inst, cmd) {
                    console.info("onOk in showDialog", inst, cmd);
                }
            })
        },
        path1(path, params) {
            apx.showFrame({
                frame: path,
                params: params,
            })
        },
    }
}
</script>

<style lang="less">

// Отладочные стили
.debug-bg {
  .jc-toolbar {
    background-color: #C6E2A6 !important;
    color: black;

    & > * {
      background-color: #d7edc5;
    }
  }

  .q-page {
    background-color: #9d569d !important;
  }

  .jc-frame--body {
    background-color: #76cacd !important;
  }

  .jc-frame {
    background-color: #d498c6 !important;
  }
}

.app2-test-89ebaa47 {


}

</style>
