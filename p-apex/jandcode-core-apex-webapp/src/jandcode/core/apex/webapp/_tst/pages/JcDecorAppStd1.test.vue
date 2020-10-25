<template>
    <tst-apex-panel class="jcdecorappstd1-test-884f6234" debug-bg>

        <template #tools>
            <tst-select v-model="cfg.toolbarSetLeft" :options="toolbarSets"
                        label="toolbar left"/>
            <tst-select v-model="cfg.toolbarSetRight" :options="toolbarSets"
                        label="toolbar right"/>
        </template>

        <div class="wrap-app">
            <App container style="height: 300px;" :own="this">
                <template #toolbar-left v-if="cfg.toolbarSetLeft">
                    <AppToolbarDemoSet :toolbarSet="cfg.toolbarSetLeft"/>
                </template>

                <template #toolbar-right>
                    <AppToolbarDemoSet :toolbarSet="cfg.toolbarSetRight"/>
                </template>

                <template #left>
                </template>

                <template #main>
                    <q-page padding>
                        <jc-btn label="hello"/>
                    </q-page>
                </template>
            </App>
        </div>

    </tst-apex-panel>
</template>

<script>
import {apex} from '../vendor'

import AppToolbarDemoSet, {createToolbarSets, SubMenu1} from './_components/AppToolbarDemoSet'

export default {
    extends: Vue.component('tst-apex-page'),
    mixins: [apex.JcApp],
    components: {
        App: apex.components.JcDecorAppStd,
        AppToolbarDemoSet,
        SubMenu1,
    },
    created() {
        this.cfgStore.applyDefault({
            toolbarSetRight: 'set1',
            toolbarSetLeft: null,
        })
    },
    data() {
        return {
            toolbarSets: createToolbarSets()
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

  .wrap-app {
    border: 1px solid gray;
    // display: inline-block;
    // width: 80vw;
  }

}

</style>
