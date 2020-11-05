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
        console.info("Frame2-created", this);
        console.info("params:", this.params);
    },

    mounted() {
        console.info("Frame1-mounted", this);
        console.info("params:", this.params);
    },
    initFrame() {
        console.info("Frame1-initFrame", this);
        console.info("params:", this.params);
    },
    data() {
        return {}
    },
    methods: {},
}
</script>
