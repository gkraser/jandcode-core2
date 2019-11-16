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
                apex.frame.showDialog(Dialog1)
            }
        }
    }
</script>

<style lang="scss">
    .jc-dialog-frame-place {
        display: flex;
    }

    .jc-frame-dialog {
        display: flex;
        flex-direction: column;
    }

    .jc-frame-page--header, .jc-frame-page--footer {
        flex-shrink: 0;
    }

    .jc-frame-page--body {
        overflow: auto;
    }
</style>
