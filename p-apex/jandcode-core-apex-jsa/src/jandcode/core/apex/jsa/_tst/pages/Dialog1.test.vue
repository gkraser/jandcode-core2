<template>
    <div>
        dialogs
        <q-btn label="show1" @click="show1"/>
    </div>
</template>

<script>
    import {apex} from './vendor'

    import Dialog1 from './Dialog1'


    export default {
        components: {
            Dialog1
        },
        props: {},
        data() {
            return {}
        },
        mounted() {
            this.show1()
        },
        methods: {
            show1() {
                apex.frame.showDialog({frame:Dialog1})
            }
        }
    }
</script>

<style lang="scss">
    .jc-frame--footer{
        //padding: 40px;

        &.q-gutter-x-sm > *, &.q-gutter-sm > * {
            //margin-left: 68px;
        }

    }
</style>
