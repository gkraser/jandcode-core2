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
        methods: {},
    }
</script>
