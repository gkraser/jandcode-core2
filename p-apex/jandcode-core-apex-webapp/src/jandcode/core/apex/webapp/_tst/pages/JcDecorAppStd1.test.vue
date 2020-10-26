<template>
    <tst-apex-panel class="jcdecorappstd1-test-884f6234" debug-bg no-padding>

        <template #tools>
            <tst-select v-model="cfg.toolbarSetLeft" :options="toolbarSets"
                        label="toolbar left"/>
            <tst-select v-model="cfg.toolbarSetRight" :options="toolbarSets"
                        label="toolbar right"/>
            <tst-checkbox label="left" v-model="cfg.left"/>
            <tst-checkbox label="title2" v-model="cfg.title2"/>
            <tst-checkbox label="frameIcon" v-model="cfg.frameIcon"/>
        </template>

        <div class="wrap-app">
            <App container style="height: calc(100vh - 72px);" :own="this">
                <template #toolbar-left v-if="cfg.toolbarSetLeft">
                    <AppToolbarDemoSet :toolbarSet="cfg.toolbarSetLeft"/>
                </template>

                <template #toolbar-right>
                    <AppToolbarDemoSet :toolbarSet="cfg.toolbarSetRight"/>
                </template>

                <template #left>
                </template>

                <template #main>
                    <Frame1 ref="frame1"/>
                </template>
            </App>
        </div>

    </tst-apex-panel>
</template>

<script>
import {apex} from '../vendor'

import AppToolbarDemoSet, {createToolbarSets, SubMenu1} from './_components/AppToolbarDemoSet'
import Frame1 from './_frames/DecorAppFrame1'

export default {
    extends: Vue.component('tst-apex-page'),
    mixins: [apex.JcApp],
    components: {
        App: apex.components.JcDecorAppStd,
        AppToolbarDemoSet,
        SubMenu1,
        Frame1,
    },
    created() {
        this.title = 'Заголовок приложения'
        this.cfgStore.applyDefault({
            toolbarSetRight: 'menu1',
            toolbarSetLeft: null,
            left: true,

            title2: false,
            
            // frame
            frameIcon: false,
        })
    },
    data() {
        return {
            toolbarSets: [null].concat(createToolbarSets())
        }
    },
    methods: {
        applyCfg() {
            let cfg = this.cfg

            this.left = cfg.left;

            this.title2 = cfg.title2 ? 'Это такой подзаголовок приложения' : null;

            //
            let frm = this.$refs.frame1
            if (frm) {
                frm.title2 = cfg.title2 ? 'Это такой фрейма подзаголовок' : null;
                frm.icon = cfg.frameIcon ? 'bus' : null;
            }
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
}

.jcdecorappstd1-test-884f6234 {

  .tst-apex-panel--body {
    //padding: 0;
  }


  .wrap-app {
    //border: 1px solid gray;
    // display: inline-block;
    // width: 80vw;
  }

}

</style>
