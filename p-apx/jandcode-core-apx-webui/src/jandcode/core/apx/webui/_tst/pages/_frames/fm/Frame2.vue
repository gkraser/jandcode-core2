<template>
    <Page>
        Frame2. params: {{ paramsStr() }}
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

    async initFrame() {
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
        }
    },
}
</script>
