<template>
    <tst-apx-panel>
        <template #tools>
        </template>

        <jc-btn label="hello"/>
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
</style>
