<template>
    <Dialog buttons="yn">
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
            this.title = 'Подтверждение'
        },
        data() {
            return {}
        },
        methods: {},
    }
</script>
