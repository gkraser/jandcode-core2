<template>
    <div class="jc-shower-main" style="display:none"></div>
</template>

<script>

import {jsaBase} from '../vendor'
import {FrameShower} from '../../baseapp/fm'

/**
 * Стандартный shower для показа страниц
 */
export class FrameShower_main_default extends FrameShower {

    constructor(own) {
        super()
        //
        this.own = own
        this.lastMountedEl = null
    }

    async showFrameWrapper(fw) {
        // сначала по быстрому монтируем фрейм
        // старый должен исчезнуть с экрана, но остался как экземпляр
        this.mountFrame(fw)

        // уничттожаем все старые
        while (this._frames.length > 0) {
            let fw = this._frames.pop()
            fw.destroy()
        }

        // сохраняем новый
        this._frames.push(fw)
    }


    closeFrameWrapper(fw, cmd) {
        //todo пока ничего не делаем
    }

    destroy() {
        this.unmountFrame()
    }

    /**
     * Отмонтировать фрейм.
     */
    unmountFrame() {
        if (this.lastMountedEl != null) {
            this.lastMountedEl.remove()
        }
        this.lastMountedEl = null
    }

    /**
     * Монтирует себе фрейм. По своему усмотреню.
     * @param fw {FrameWrapper} ссылка на фрейм
     */
    mountFrame(fw) {
        this.unmountFrame()
        //
        let parentEl = this.own.$el.parentNode
        let frameEl = fw.getEl()
        //
        if (this.own.syncMinHeight) {
            frameEl.style.minHeight = parentEl.style.minHeight
        }
        parentEl.insertAdjacentElement('afterbegin', frameEl)
        this.lastMountedEl = frameEl
    }

}

/**
 * shower 'main' по умолчанию.
 * Монтирует фрейм в начало своего родительского элемента.
 * Сам - невидим.
 */
export default {
    name: 'jc-shower-main',

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
        this.shower = new FrameShower_main_default(this)
    },
    mounted() {
        jsaBase.app.frameManager.registerShower('main', this.shower)
    },
    beforeDestroy() {
        jsaBase.app.frameManager.unregisterShower(this.shower)
        this.shower.destroy()
        this.shower = null
    },
}
</script>
