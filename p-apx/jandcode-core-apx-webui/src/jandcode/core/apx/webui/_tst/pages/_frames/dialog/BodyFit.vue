<template>
    <Dialog body-fit _style="width:500px;height:400px"
            :size="sizeMax?'max':null"
            :no-padding="noPadding">
        <div style="background-color: #a5d6a7">
            body element
            <tst-checkbox label="size: max" v-model="sizeMax"/>
            <tst-checkbox label="no padding" v-model="noPadding"/>
        </div>
    </Dialog>
</template>

<script>
import {apx} from '../vendor'

export default {
    extends: apx.JcFrame,
    created() {
        this.title = 'BodyFit.vue'
    },
    frameInit() {
    },
    data() {
        return {
            sizeMax: false,
            noPadding: true,
        }
    },
    methods: {},
}
</script>
