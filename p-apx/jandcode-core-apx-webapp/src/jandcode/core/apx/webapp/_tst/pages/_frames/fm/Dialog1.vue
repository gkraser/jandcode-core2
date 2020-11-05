<template>
    <Dialog>
        Dialog1
    </Dialog>
</template>

<script>
import {apx} from '../vendor'

export default {
    extends: apx.JcFrame,
    props: {},
    created() {
        this.title = 'Dialog1.vue'
    },
    initFrame() {
    },
    data() {
        return {}
    },
    methods: {
        onOk(inst, cmd) {
            console.info("onOk in Dialog1", inst, cmd);
        }
    },
}
</script>
