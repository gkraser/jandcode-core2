<template>
    <div class="jc-shower-main" style="display:none"></div>
</template>

<script>

import {jsaBase, Quasar} from '../vendor'
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
 * Может синхронизировать min-height для фрейма с min-height родительского элемента.
 * Это используется, когда shower внутри q-page (место по умолчанию).
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

    created() {
        this.lastMountedEl = null
        this.resizeObs = null
        this.needSyncMinHeight = false
        this.shower = new FrameShower_main_default(this)
    },

    mounted() {
        jsaBase.app.frameManager.registerShower('main', this.shower)
    },

    beforeDestroy() {
        if (this.resizeObs) {
            this.resizeObs.$destroy()
        }
        jsaBase.app.frameManager.unregisterShower(this.shower)
        this.shower.destroy()
        this.shower = null
    },

    methods: {

        /**
         * Возвращает настроенный экземпляр QResizeObserver, если нужно.
         * Если не нужно - возвращает null.
         */
        getResizeObs() {
            if (this.syncMinHeight && !this.resizeObs) {
                this.resizeObs = new Quasar.QResizeObserver()
                this.resizeObs.$on('resize', (ev) => {
                    if (this.needSyncMinHeight) {
                        let need = this.needSyncMinHeight
                        let parentMh = this.$el.parentNode.style.minHeight
                        let curMh = this.lastMountedEl.style.minHeight
                        if (curMh) {
                            // уже установлена min-height
                            try {
                                let parentMhInt = parseInt(parentMh, 10)
                                let curMhInt = parseInt(curMh, 10)
                                if (curMhInt > parentMhInt) {
                                    // min-height фрейма больше, не трогаем
                                    need = false
                                }
                            } catch(e) {
                                // ignore
                            }
                        }
                        if (need) {
                            this.lastMountedEl.style.minHeight = parentMh
                        }
                    }
                })
                let roEl = document.createElement('div')
                roEl.style.display = 'none'
                this.$el.parentNode.appendChild(roEl)
                this.resizeObs.$mount(roEl)
            }
            return this.resizeObs
        },

        unmountFrame() {
            if (this.lastMountedEl != null) {
                this.lastMountedEl.remove()
            }
            this.lastMountedEl = null
            this.needSyncMinHeight = false
        },

        mountFrame(fw) {
            this.unmountFrame()
            //
            let parentEl = this.$el.parentNode
            let frameEl = fw.getEl()
            let frameEl_exists = true
            if (!frameEl) {
                // первый раз
                frameEl = document.createElement("div")
                parentEl.insertAdjacentElement('afterbegin', frameEl)
                fw.frameInst.$mount(frameEl)
                frameEl_exists = false
                // после монтирования - элемент другой!
                frameEl = fw.getEl()
            }
            //
            if (this.syncMinHeight) {
                let mh = frameEl.style.minHeight
                if (!mh) {
                    // для фрейма явно не установлено min-height
                    frameEl.style.minHeight = parentEl.style.minHeight
                    // включаем observer
                    this.getResizeObs()
                    this.needSyncMinHeight = true
                }
            }
            if (frameEl_exists) {
                // не сейчас создан, добавляем
                parentEl.insertAdjacentElement('afterbegin', frameEl)
            }
            this.lastMountedEl = frameEl
        },

    }
}
</script>
