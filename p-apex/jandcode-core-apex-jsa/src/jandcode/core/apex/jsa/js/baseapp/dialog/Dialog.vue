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
        mounted() {
            console.info("Dialog mounted");
        },
        destroyed() {
            console.info("Dialog destroyed");
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
                console.info("Dialog beforeShowDialog");
            },

            onHideDialog() {
                console.info("Dialog hide");
                this.$emit('dialog-close', this)
            }
        }
    }
</script>
