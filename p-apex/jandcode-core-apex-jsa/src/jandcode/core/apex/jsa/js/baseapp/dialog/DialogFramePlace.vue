<template>
    <div class="jc-dialog-frame-place">
    </div>
</template>

<script>
    /**
     * Место для фрейма в диалоге.
     */
    export default {
        props: {

            /**
             * Владелец.
             * Должен иметь свойство frameInst с экземпляром фрейма.
             */
            own: Object,

        },

        mounted() {
            this.$el.appendChild(this.own.frameInst.$el)
        },

        data() {
            return {}
        }
    }
</script>
