import Page from './JcDecorFramePage'
import Dialog from './JcDecorFrameDialog'
import {jsaBase} from '../vendor'
import upperFirst from 'lodash/upperFirst'

/**
 * Базовый предок для фреймов
 */
export default {

    components: {
        Page: Page,
        Dialog: Dialog,
    },

    data() {
        return {
            title: 'Без заголовка',
            title2: '',
            icon: 'frame',
        }
    },

    methods: {

        /**
         * Закрыть фрейм с указанной командой.
         * @param cmd команда
         */
        closeFrame(cmd) {
            let th = this

            if (!cmd) {
                cmd = 'cancel'
            }
            let handlerName = 'on' + upperFirst(cmd)

            let handleProcess = (handlerResult) => {
                if (handlerResult === false) {
                    // закрывать нельзя
                } else if (handlerResult instanceof Promise) {
                    // ждем окончания promise
                    handlerResult.then((result) => {
                        if (result === false) {
                            // promise вернул false, закрывать нельзя
                            return
                        }
                        th.shower.closeFrame(cmd)
                    })
                } else {
                    // можно закрывать
                    th.shower.closeFrame(cmd)
                }
            }

            if (jsaBase.isFunction(this[handlerName])) {
                // есть обработчик onXxx
                handleProcess(this[handlerName](cmd))

            } else if (jsaBase.isFunction(this.onCmd)) {
                handleProcess(this.onCmd(cmd))

            } else {
                // нет обработчиков
                th.shower.closeFrame(cmd)
            }

        }

    }

}
