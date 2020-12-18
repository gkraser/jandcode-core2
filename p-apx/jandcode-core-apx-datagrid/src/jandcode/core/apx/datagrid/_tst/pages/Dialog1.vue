<template>
    <Dialog body-fit no-padding style="width: 700px;height:400px" _size="max">
        <jc-datagrid :options="opt1" style1="width:500px"/>
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
    frameInit() {
        this.opt1 = grids.grid1({})
    },
    data() {
        return {}
    },
    methods: {},
}
</script>
