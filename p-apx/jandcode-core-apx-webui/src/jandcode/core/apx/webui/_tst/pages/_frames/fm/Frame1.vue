<template>
    <Page>
        Frame1. params: {{ paramsStr() }}
    </Page>
</template>

<script>
import {apx} from '../vendor'

export default {
    extends: apx.JcFrame,
    props: {},
    created() {
        this.title = 'Frame1 (' + apx.jsaBase.nextId("") + ")"
        console.info("Frame1-created", this);
        console.info("params:", this.params);
        console.info("shower:", this.frameWrapper.shower);
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
    methods: {
        paramsStr() {
            return JSON.stringify(this.params)
        }
    },
}
</script>
