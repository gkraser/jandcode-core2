<template>
    <Page>
        Frame2. params: {{ paramsStr() }}
        <br>
        <jc-btn label="showOtherFrame" @click="showOtherFrame"/>
        <jc-btn label="showDialog1" @click="showDialog1"/>
    </Page>
</template>

<script>
import {apx, jsaBase} from '../vendor'

export default {
    extends: apx.JcFrame,
    props: {},

    created() {
        this.title = 'Frame2 (' + apx.jsaBase.nextId("") + ")"
        console.info("Frame2-created", this);
        console.info("params:", this.params);
    },

    mounted() {
        console.info("Frame2-mounted", this);
        console.info("params:", this.params);
    },

    async frameInit() {
        let act = "jandcode/core/apx/webui/_tst/pages/_actions/action-json1.json"

        let res1 = await jsaBase.ajax.request({
            url: act,
            params: {
                cnt: this.params.cnt || 2,
                pause: this.params.pause || 200,
            }
        })
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
                frame: 'jandcode/core/apx/webui/_tst/pages/_frames/fm/Frame1.vue'
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
