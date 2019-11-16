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
        mounted(){
            this.show1()
        },
        methods: {
            show1() {
                apex.frame.showDialog(Dialog1)
            }
        }
    }
</script>

<style lang="scss">
    .jc-dialog-frame-place {
        background-color: #ffd0a4;
        display: flex;
    }

    .jc-frame-dialog{
        background-color: #f3ffd3;
        display: flex;
        flex-direction: column;
        flex-grow:1;
        //height: 200px;
        overflow: auto;
    }

    .jc-frame-page--header, .jc-frame-page--footer {
        background-color: bisque;
    }

    .jc-frame-page--body{
        background-color: #d1deff;
        overflow: auto;
        flex-grow:1;
        //height: 200px;
    }
</style>
