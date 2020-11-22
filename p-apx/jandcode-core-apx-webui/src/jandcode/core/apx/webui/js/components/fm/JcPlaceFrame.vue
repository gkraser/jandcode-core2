<template>
    <div class="jc-place-frame" style="display:none"></div>
</template>

<script>

import {jsaBase} from '../vendor'

/**
 * Монтировщик фреймов по умолчанию.
 * Монтирует фрейм в начало своего родительского элемента.
 * Сам - невидим.
 */
export default {
    name: 'jc-place-frame',

    props: {
        /**
         * Синхронизировать ли min-height с родительским элементом.
         * По умолчанию - true.
         */
        syncMinHeight: {
            type: Boolean,
            default: true,
        }
    },
    data() {
        return {}
    },
    created() {
        this.lastMountedEl = null
    },
    mounted() {
        jsaBase.app.frameManager.registerPlaceFrame(this)
    },
    beforeDestroy() {
        this.unmountFrame()
        jsaBase.app.frameManager.unregisterPlaceFrame(this)
    },
    methods: {

        /**
         * Отмонтировать фрейм.
         */
        unmountFrame() {
            if (this.lastMountedEl != null) {
                this.lastMountedEl.remove()
            }
            this.lastMountedEl = null
        },

        /**
         * Монтирует себе фрейм. По своему усмотреню.
         * @param fi {FrameWrapper} ссылка на фрейм
         */
        mountFrame(fi) {
            this.unmountFrame()
            //
            let parentEl = this.$el.parentNode
            let frameEl = fi.getEl()
            //
            if (this.syncMinHeight) {
                frameEl.style.minHeight = parentEl.style.minHeight
            }
            parentEl.insertAdjacentElement('afterbegin', frameEl)
            this.lastMountedEl = frameEl
        }

    },
}
</script>
