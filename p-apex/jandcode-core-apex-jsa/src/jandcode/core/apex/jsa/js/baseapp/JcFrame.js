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

            console.info("closeFrame with cmd", cmd);
            if (!cmd) {
                cmd = 'cancel'
            }
            let handlerName = 'on' + upperFirst(cmd)

            let handleProcess = (handlerResult) => {
                if (handlerResult === false) {
                    // закрывать нельзя
                    console.info("NO need close");
                    return
                } else if (handlerResult instanceof Promise) {
                    console.info("promise wait");
                    // ждем окончания
                    handlerResult.then((result) => {
                        console.info("promise then");
                        if (result === false) {
                            return
                        }
                        th.shower.closeFrame(cmd)
                    })
                } else {
                    // можно закрывать
                    console.info("need close");
                    th.shower.closeFrame(cmd)
                }
            }

            if (jsaBase.isFunction(this[handlerName])) {
                // есть обработчик onXxx
                handleProcess(this[handlerName](cmd))

            } else if (jsaBase.isFunction(this.onCmd)) {
                handleProcess(this.onCmd(cmd))

            } else {
                // не обработчиков
                console.info("need close no handlers");
                th.shower.closeFrame(cmd)
            }

        }

    }

}
