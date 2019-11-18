<template>
    <div>
        dialogs
        <q-btn label="show1" @click="show1"/>
        <q-btn label="DialogOnXxx1" @click="show2"/>
    </div>
</template>

<script>
    import {apex} from './vendor'

    import Dialog1 from './Dialog1'
    import DialogOnXxx1 from './DialogOnXxx1'


    export default {
        components: {
            Dialog1,
            DialogOnXxx1,
        },
        props: {},
        data() {
            return {}
        },
        mounted() {
            this.show2()
        },
        methods: {
            show1() {
                apex.frame.showDialog({frame:Dialog1})
            },
            show2() {
                apex.frame.showDialog({frame:DialogOnXxx1})
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
