<template>
    <App>
        <template #toolbar-right>
            <jc-toolbar>
                <jc-action icon="bus"/>
            </jc-toolbar>
        </template>

        <template #left>
        </template>

    </App>
</template>

<script>
import {apx} from './vendor'

export default {
    extends: apx.JcApp,
    components: {},
    created() {
        this.title = 'XxxYyy'
        this.left = true
    },
    data() {
        return {}
    },
    methods: {}
}
</script>

