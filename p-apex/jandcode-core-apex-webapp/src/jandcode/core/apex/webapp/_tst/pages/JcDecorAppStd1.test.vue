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
            <tst-select v-model="cfg.frameToolbar" :options="toolbarSets"
                        label="frameToolbar"/>
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
                    <q-item-label header>Меню</q-item-label>
                    <SideMenu1 :items="sideMenu_itemsSet.items1" :levels="3"/>
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
import SideMenu1 from './_components/SideMenu1'
import Frame1 from './_frames/DecorAppFrame1'

let sideMenu_itemsSet = {
    items1: [
        {label: 'Элемент c иконкой', icon: 'bus'},
        {label: 'Элемент без иконки', icon: '', opened: true},
        {label: 'Элемент с svg', icon: 'svg1'},
        {label: 'Элемент с png', icon: 'png1'},
    ],

    itemsNoIcon: [
        {label: 'Элемент 1'},
        {label: 'Элемент 2'},
        {label: 'Элемент 3'},
        {label: 'Элемент 4'},
    ],

    itemsFs: [
        {label: 'Папка 1', icon: 'folder1'},
        {label: 'Папка 2', icon: 'folder1'},
        {label: 'Файл 3', icon: 'file1'},
        {label: 'Файл 4', icon: 'file1'},
    ],
}

export default {
    extends: Vue.component('tst-apex-page'),
    mixins: [apex.JcApp],
    components: {
        App: apex.components.JcDecorAppStd,
        AppToolbarDemoSet,
        SubMenu1,
        Frame1,
        SideMenu1,
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
            frameToolbar: null,
        })
    },
    data() {
        return {
            toolbarSets: [null].concat(createToolbarSets()),
            sideMenu_itemsSet: sideMenu_itemsSet,
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
                frm.toolbarSet = cfg.frameToolbar;
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
