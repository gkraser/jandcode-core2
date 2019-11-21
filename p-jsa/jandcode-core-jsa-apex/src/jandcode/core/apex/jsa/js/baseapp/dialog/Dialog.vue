<template>
    <q-dialog ref="dialogInst"
              @hide="onHideDialog"
              content-class="jc-dialog">
        <DialogFramePlace :own="this">
        </DialogFramePlace>
    </q-dialog>
</template>

<script>

    // Место для фрейма в диалоге
    let DialogFramePlace = {
        props: {
            own: Object,
        },

        render(h) {
            return h('div', {style: {display: 'none'}})
        },

        mounted() {
            // монтируем фрейм вместо себя
            this.$el.parentNode.appendChild(this.own.frameInst.$el)
            this.$el.parentNode.removeChild(this.$el)
        },
    }

    export default {
        components: {
            DialogFramePlace
        },
        props: {
            frameInst: Object
        },
        data() {
            return {}
        },
        methods: {
            showDialog() {
                this.$refs.dialogInst.show()
            },

            hideDialog() {
                this.$refs.dialogInst.hide()
            },

            onHideDialog() {
                this.$emit('dialog-close', this)
            }
        }
    }
</script>
