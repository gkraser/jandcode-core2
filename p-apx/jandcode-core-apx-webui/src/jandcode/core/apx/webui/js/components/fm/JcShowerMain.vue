<template>
    <div class="jc-shower-main" style="display:none"></div>
</template>

<script>

import {jsaBase} from '../vendor'
import {FrameShower} from '../../baseapp/fm'
import upperFirst from 'lodash/upperFirst'

/**
 * Стандартный shower для показа страниц
 */
export class FrameShower_main_default extends FrameShower {

    constructor(own) {
        super()
        //
        this.own = own
    }

    async showFrameWrapper(fw) {
        // сначала по быстрому монтируем фрейм
        // старый должен исчезнуть с экрана, но остался как экземпляр
        this.own.mountFrame(fw)

        // нужно ли помещать в стек
        let isStack = fw.options.stack

        if (!isStack) {
            // уничттожаем все старые, если новый не хочет быть в стеке
            while (this._frames.length > 0) {
                let fw = this._frames.pop()
                fw.destroy()
            }
        }

        // сохраняем новый
        this._frames.push(fw)

        // меняем url, если допустимо
        let routePath = fw.getRoutePath()
        if (routePath != null) {
            this.frameManager.history.updateHash(routePath)
        }

    }

    async closeFrameWrapper(fw, cmd) {
        if (this._frames.length <= 1) {
            // стек либо пустой, либо там только один фрейм - ничего не делаем
            return
        }
        let idx = this._frames.indexOf(fw)
        if (idx === -1) {
            // этот фрейм не в стеке
            return
        }

        // проверяем возможность закрытся. Почти как в диалоге
        if (!cmd) {
            cmd = 'cancel'
        }
        let frameInst = fw.frameInst
        let handlerName = 'on' + upperFirst(cmd)

        if (jsaBase.isFunction(frameInst[handlerName])) {
            // у фрейма есть обработчик onXxx
            if (await frameInst[handlerName](frameInst, cmd) === false) {
                return  // закрываться нельзя
            }
        } else if (jsaBase.isFunction(frameInst.onCmd)) {
            // у фрейма есть обработчик onCmd
            if (await frameInst.onCmd(frameInst, cmd) === false) {
                return  // закрываться нельзя
            }
        }

        // все разрешили закрытся, закрываем

        // это удаляемый
        let removedFw = this._frames[idx]
        // удаляем его из стека
        this._frames.splice(idx, 1)
        // это который сверху в стеке
        let topFw = this._frames[this._frames.length - 1]
        // монтируем верхний
        this.mountFrame(topFw)
        // уничтожаем старый
        removedFw.destroy()
    }

    destroy() {
        this.own.unmountFrame()
        // уничтожаем все фреймы
        for (let fw of this._frames) {
            fw.destroy()
        }
        this._frames = null
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
        this.lastMountedEl = null
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
    methods: {

        unmountFrame() {
            if (this.lastMountedEl != null) {
                this.lastMountedEl.remove()
            }
            this.lastMountedEl = null
        },

        mountFrame(fw) {
            this.unmountFrame()
            //
            let parentEl = this.$el.parentNode
            let frameEl = fw.getEl()
            //
            if (this.syncMinHeight) {
                frameEl.style.minHeight = parentEl.style.minHeight
            }
            parentEl.insertAdjacentElement('afterbegin', frameEl)
            this.lastMountedEl = frameEl
        }

    }
}
</script>
