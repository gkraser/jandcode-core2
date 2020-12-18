<template>
    <Dialog>
        Это текст простой
        <tst-big-content/>
    </Dialog>
</template>

<script>
import {apx} from '../vendor'

export default {
    extends: apx.JcFrame,
    created() {
        this.title = 'ToContent'
    },
    frameInit() {
    },
    data() {
        return {}
    },
    methods: {},
}
</script>
