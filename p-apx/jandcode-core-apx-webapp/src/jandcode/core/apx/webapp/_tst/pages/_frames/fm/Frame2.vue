<template>
    <Page>
        Frame2
    </Page>
</template>

<script>
import {apx, jsaBase} from '../vendor'

export default {
    extends: apx.JcFrame,
    props: {},
    created() {
        this.title = 'Frame2.vue'
    },
    mounted() {
    },
    async initFrame() {
        let act = "jandcode/core/apx/webapp/_tst/pages/_frames/fm/action-json1.json"

        let res1 = await jsaBase.ajax.request({
            url: act,
            params: {
                cnt: 2
            }
        })
    },
    data() {
        return {}
    },
    methods: {},
}
</script>
