<template>
    <tst-apx-panel class="jcpanel1-test-70b5b71e">
        <template #tools>
        </template>

        <div class="row q-gutter-x-md">

            <jc-panel class="col" title="Это панель 1">
                тело в секции
            </jc-panel>

            <jc-panel class="col" title="Это панель 2">
                <template #toolbar>
                    <jc-action icon="more-v"/>
                    <jc-action icon="bus" label="Привет"/>
                </template>

                тело в секции

            </jc-panel>

            <jc-panel class="col">
                тело в секции
            </jc-panel>

            <jc-panel class="col">
                <template #body>
                    тело БЕЗ секции
                </template>
            </jc-panel>

            <jc-panel class="col">
                <template #toolbar>
                    <jc-action icon="more-v"/>
                </template>
                без заголовка, тело в секции
            </jc-panel>
        </div>

    </tst-apx-panel>
</template>

<script>
import {tst} from '../vendor'

export default {
    mixins: [tst.mixins.cfgStore],
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

.jcpanel1-test-70b5b71e {


}

</style>
