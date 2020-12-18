<template>
    <Page>
        Frame1. params: {{ paramsStr() }}
        <br>
        <jc-btn label="showOtherFrame" @click="showOtherFrame"/>
        <jc-btn label="showDialog1" @click="showDialog1"/>
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
    frameInit() {
        console.info("Frame1-frameInit", this);
        console.info("params:", this.params);
    },
    data() {
        return {}
    },
    methods: {
        paramsStr() {
            return JSON.stringify(this.params)
        },
        showOtherFrame() {
            this.showFrame({
                frame: 'jandcode/core/apx/webui/_tst/pages/_frames/fm/Frame2.vue'
            })
        },
        showDialog1() {
            this.showDialog({
                frame: 'jandcode/core/apx/webui/_tst/pages/_frames/fm/Dialog1.vue'
            })
        },
    },
}
</script>
