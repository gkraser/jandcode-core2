<template>
    <Dialog body-fit style="width: 700px;height:400px">
        <jc-datagrid :options="opt1"/>
    </Dialog>
</template>

<script>
import {apx} from '../vendor'
import * as grids from './grids'

export default {
    extends: apx.JcFrame,
    created() {
        this.title = 'Dialog1.vue'
    },
    initFrame() {
        this.opt1 = grids.grid1({})
    },
    data() {
        return {}
    },
    methods: {},
}
</script>
