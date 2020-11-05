<template>
    <Page>
        Frame1
    </Page>
</template>

<script>
import {apx} from '../vendor'

export default {
    extends: apx.JcFrame,
    props: {},
    created() {
        this.title = 'Frame1.vue'
    },
    initFrame() {
    },
    data() {
        return {}
    },
    methods: {},
}
</script>
