<template>
    <Page>
        Home
    </Page>
</template>

<script>
import {apx} from '../vendor'

export default {
    extends: apx.JcFrame,
    props: {},
    created() {
        this.title = 'Home.vue'
    },
    destroyed() {
        console.info("home destroyed", this);
    },
    frameInit() {
    },
    data() {
        return {}
    },
    methods: {},
}
</script>
