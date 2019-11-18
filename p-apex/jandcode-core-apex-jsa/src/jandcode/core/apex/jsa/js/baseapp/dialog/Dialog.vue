<template>
    <q-dialog ref="dialogInst" @before-show="beforeShowDialog" @hide="onHideDialog"
              content-class="jc-dialog">
        <DialogFramePlace :own="this">
        </DialogFramePlace>
    </q-dialog>
</template>

<script>
    import DialogFramePlace from './DialogFramePlace'

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

            beforeShowDialog() {
            },

            onHideDialog() {
                this.$emit('dialog-close', this)
            }
        }
    }
</script>
