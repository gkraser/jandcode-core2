<template>
    <tst-apex-panel class="toolbar-610ebf21" debug-bg>
        <template #tools>
            <tst-select v-model="cfg.toolbarSet" :options="toolbarSets"
                        label="toolbarSet"/>
        </template>

        sdsd
        <div class="row items-center q-gutter-x-sm q-mb-md">
            <div>action без toolbar:</div>
            <jc-action icon="bus"/>
            <jc-action icon="star"/>
            <jc-action icon="mail"/>
            <jc-action label="Конфигурации" icon="config"/>
            <jc-action label="Конфигурации" icon="config" color="primary"/>
            <jc-action label="Задачи"/>
            <jc-action label="Меню 1">
                <SubMenu1/>
            </jc-action>
            <jc-action icon="bus">
                <SubMenu1/>
            </jc-action>
            <jc-action label="Меню 2" icon="mail">
                <SubMenu1/>
            </jc-action>
        </div>
        <div class="column q-mt-sm q-gutter-y-md toolbar-place">
            <AppToolbarDemoSet :toolbarSet="cfg.toolbarSet"/>
            <AppToolbarDemoSet toolbarSet="menu1"/>
            <AppToolbarDemoSet toolbarSet="logo1"/>
        </div>
    </tst-apex-panel>
</template>

<script>
import AppToolbarDemoSet, {createToolbarSets, SubMenu1} from './_components/AppToolbarDemoSet'

export default {
    extends: Vue.component('tst-apex-page'),
    components: {
        AppToolbarDemoSet,
        SubMenu1,
    },
    created() {
        this.cfgStore.applyDefault({
            toolbarSet: 'set1',
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

.debug-arial {
  font-family: 'Arial', sans-serif !important;
}

.toolbar-610ebf21 {
}

</style>


