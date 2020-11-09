<template>
    <tst-apx-panel class="toolbar-610ebf21" debug-bg>
        <template #tools>
            <tst-select v-model="cfg.toolbarSet" :options="toolbarSets"
                        label="toolbarSet"/>
            <tst-select v-model="cfg.titleSize" :options="titleSizes"
                        label="titleSizes"/>
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
            <AppToolbarDemoSet :toolbarSet="cfg.toolbarSet" :titleSize="cfg.titleSize"/>
            <AppToolbarDemoSet toolbarSet="menu1" :titleSize="cfg.titleSize"/>
            <AppToolbarDemoSet toolbarSet="logo1" :titleSize="cfg.titleSize"/>
        </div>
    </tst-apx-panel>
</template>

<script>
import AppToolbarDemoSet, {createToolbarSets, SubMenu1} from './_components/AppToolbarDemoSet'

export default {
    extends: Vue.component('tst-apx-page'),
    components: {
        AppToolbarDemoSet,
        SubMenu1,
    },
    created() {
        this.cfgStore.applyDefault({
            toolbarSet: 'set1',
            titleSize: null,
        })
    },
    data() {
        return {
            toolbarSets: createToolbarSets(),
            titleSizes: [null, 'lg', 'sm']
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


