<template>
    <tst-apx-panel class="badge1-test-7f21406c" debug-bg>
        <template #tools>
            <tst-fontsize/>
        </template>

        <div class="place row q-gutter-md items-start">
            <q-badge color="primary">
                #4D96F2
            </q-badge>

            <div class="text-h1">
                Badge
                <q-badge color="primary">v1.0.0+</q-badge>
            </div>

            <q-btn label="QBtn">
                <q-badge label="12" floating/>
            </q-btn>

            <q-btn icon="mail" flat>
                <q-badge label="9" floating/>
            </q-btn>

            <jc-btn label="JcBtn">
                <q-badge label="12" floating color="accent"/>
            </jc-btn>

            <jc-btn icon="mail" flat>
                <q-badge label="9" floating color="accent-200"/>
            </jc-btn>

            <jc-action label="JcAction">
                <template #content>
                    <q-badge label="12" floating color="warning"/>
                </template>
            </jc-action>

            <jc-action icon="mail" flat>
                <template #content>
                    <q-badge label="9" floating color="warning-300"/>
                </template>
            </jc-action>

            <jc-toolbar>
                <label>JcToolbar:</label>
                <jc-action label="JcAction">
                    <template #content>
                        <q-badge label="12" floating color="warning"/>
                    </template>
                </jc-action>

                <jc-action icon="mail" flat>
                    <template #content>
                        <q-badge label="9" floating color="warning-300"/>
                    </template>
                </jc-action>
            </jc-toolbar>
        </div>

    </tst-apx-panel>
</template>

<script>
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
    }
}
</script>

<style lang="less">

// Отладочные стили
.debug-bg {
  .place {
    & > * {
      background-color: #d7edc5 !important;
    }
  }

  .jc-toolbar {
    & > * {
      background-color: #acbe9d;
    }
  }

}


.badge1-test-7f21406c {


}

</style>
