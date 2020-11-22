<template>
    <Dialog buttons="ok">
        {{ text }}
    </Dialog>
</template>

<script>
import {JcFrame} from '../../baseapp'

export default {
    extends: JcFrame,
    created() {
        this.title = 'Ошибка'
        this.text = this.params.text || 'Нет текста'
    },
    data() {
        return {
            text: ''
        }
    },
    methods: {},
}
</script>
