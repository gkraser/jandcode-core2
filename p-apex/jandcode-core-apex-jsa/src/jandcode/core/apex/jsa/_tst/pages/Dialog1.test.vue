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
        methods: {
            show1() {
                apex.frame.showDialog(Dialog1)
            }
        }
    }
</script>
