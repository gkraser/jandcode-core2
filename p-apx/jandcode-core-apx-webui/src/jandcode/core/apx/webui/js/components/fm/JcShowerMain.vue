<template>
    <div class="jc-shower-main" style="display:none"></div>
</template>

<script>

import {jsaBase, Quasar} from '../vendor'
import {FrameShower} from '../../baseapp/fm'
import upperFirst from 'lodash/upperFirst'

/**
 * Стандартный shower для показа страниц
 *
 * При изменении состава фреймов уведомляет шину событий
 * событием: "shower.main.change-frames"
 */
export class FrameShower_main_default extends FrameShower {

    constructor(own) {
        super()
        //
        this.own = own
    }

    async showFrameWrapper(fw) {
        // нужно ли помещать в стек
        let isStack = fw.options.stack

        let old_frames = null
        if (!isStack) {
            old_frames = this._frames
            this._frames = []
        }

        // сохраняем новый
        this._frames.push(fw)

        // сначала по быстрому монтируем фрейм
        // старый должен исчезнуть с экрана, но остался как экземпляр
        this.own.mountFrame(fw)

        if (old_frames != null) {
            // уничттожаем все старые, если новый не хочет быть в стеке
            while (old_frames.length > 0) {
                let fw = old_frames.pop()
                fw.destroy()
            }
        }

        // меняем url, если допустимо
        let routePath = fw.getRoutePath()
        if (routePath != null) {
            this.frameManager.history.updateHash(routePath)
        }

        // уведомляем
        jsaBase.app.eventBus.$emit("shower.main.change-frames", this)
    }

    /**
     * Проверить возможность закрыть фрейм
     */
    async checkForClose(fw, cmd) {
        // проверяем возможность закрыться. Почти как в диалоге
        if (!cmd) {
            cmd = 'cancel'
        }
        let frameInst = fw.frameInst
        let handlerName = 'on' + upperFirst(cmd)

        if (jsaBase.isFunction(frameInst[handlerName])) {
            // у фрейма есть обработчик onXxx
            if (await frameInst[handlerName](frameInst, cmd) === false) {
                return false // закрываться нельзя
            }
        } else if (jsaBase.isFunction(frameInst.onCmd)) {
            // у фрейма есть обработчик onCmd
            if (await frameInst.onCmd(frameInst, cmd) === false) {
                return false // закрываться нельзя
            }
        }

        // закрывать можно
        return true
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

        // проверяем возможность закрыться. Почти как в диалоге
        if (!await this.checkForClose(fw)) {
            return // закрываться нельзя
        }

        // все разрешили закрытся, закрываем

        // это удаляемый
        let removedFw = this._frames[idx]
        // удаляем его из стека
        this._frames.splice(idx, 1)
        // это который сверху в стеке
        let topFw = this._frames[this._frames.length - 1]
        // монтируем верхний
        this.own.mountFrame(topFw)
        // уничтожаем старый
        removedFw.destroy()

        // уведомляем
        jsaBase.app.eventBus.$emit("shower.main.change-frames", this)
    }

    isFrameWrapperClosable(fw) {
        console.info("check", fw, this._frames);
        if (this._frames.length <= 1) {
            return false
        }
        let idx = this._frames.indexOf(fw)
        // фрейм показан и ион не первый в стеке
        return idx > 0
    }

    /**
     * Активировать фрейм.
     * Все, которые после него - закрыть.
     * @param fw
     */
    async activateFrameWrapper(fw) {
        if (this._frames.length <= 1) {
            // стек либо пустой, либо там только один фрейм - ничего не делаем
            return
        }
        let idx = this._frames.indexOf(fw)
        if (idx === -1) {
            // этот фрейм не в стеке
            return
        }
        if (idx === this._frames.length - 1) {
            // это последний фрейм в стеке, он и так активный
            return
        }

        // проверяем возможность закрытия для всех после искомого
        let curIdx = this._frames.length - 1
        let closeIdx = null
        while (curIdx > idx) {
            if (!await this.checkForClose(this._frames[curIdx])) {
                break
            }
            closeIdx = curIdx
            curIdx--
        }

        if (closeIdx == null) {
            // нечего закрывать
            return
        }

        // монтируем активный
        this.own.mountFrame(this._frames[closeIdx - 1])

        // остальные уничтожаем
        while (this._frames.length > closeIdx) {
            let fwd = this._frames.pop()
            fwd.destroy()
        }

        // уведомляем
        jsaBase.app.eventBus.$emit("shower.main.change-frames", this)
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
        jsaBase.app.eventBus.$emit("shower.main.change-frames", this.shower)
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
