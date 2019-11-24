<template>
    <Dialog buttons="ok">
        {{text}}
    </Dialog>
</template>

<script>
    import {JcFrame} from '../../baseapp'

    export default {
        extends: JcFrame,
        props: {
            text: {}
        },
        created() {
            this.title = 'Сообщение'
        },
        data() {
            return {}
        },
        methods: {},
    }
</script>
