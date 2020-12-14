<template>
    <Dialog body-fit style="width:500px;height:400px">
        <div style="background-color: #a5d6a7">
            body element
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
    initFrame() {
    },
    data() {
        return {}
    },
    methods: {},
}
</script>
