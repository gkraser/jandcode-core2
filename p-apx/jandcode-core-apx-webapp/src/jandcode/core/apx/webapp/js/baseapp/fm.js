/*

Фреймы

----------------------------------------------------------------------------- */

import {jsaBase, Vue} from '../vendor'
import Dialog from './dialog/Dialog'
import upperFirst from 'lodash/upperFirst'

// опция initFrame будет выглядеть как массив (аналогично другим life-cycle hookd)
Vue.config.optionMergeStrategies.initFrame = Vue.config.optionMergeStrategies.created

/**
 * Сервис для менеджера фреймов
 */
export class FrameManagerService extends jsaBase.AppService {

    onCreate() {
        /**
         * Текущий менеджер фреймов
         * @type {FrameManager}
         * @name frameManager
         * @memberOf jsaBase.app
         */
        this.app.frameManager = new FrameManager()
    }

}

export class FrameManager {

    constructor() {
        // места монтирования фреймов
        this._framePlaces = []
        // все текущие работающие фреймы
        this._frames_page = []
        this._frames_dialog = []
    }

    /**
     * Показать фрейм
     * @param fi {FrameItem}
     */
    async showFrame(fi) {
        // делаем класс компонента
        await this._resolveFrameComp(fi)

        // создаем экземпляр
        fi.frameInst = new fi.frameCompCls({propsData: fi.propsData})

        // инициализаируем (возможна интенсивная загрузка данных)
        await fi.initFrame()

        // теперь фрейм готов к работе
        fi.frameInst.$mount()

        // присоединяем себя перед самым показом
        fi.frameManager = this
        fi.frameInst.frameManager = this

        // показываем его
        if (fi instanceof FrameItemDialog) {
            await this._showFrame_dialog(fi)
        } else {
            await this._showFrame_page(fi)
        }

    }

    //////

    registerPlaceFrame(inst) {
        if (this._framePlaces.indexOf(inst) !== -1) {
            return // уже есть такой же
        }
        this._framePlaces.push(inst)
    }

    unregisterPlaceFrame(inst) {
        let a = this._framePlaces.indexOf(inst)
        if (a === -1) {
            return
        }
        this._framePlaces.splice(a, 1)
    }

    //////

    /**
     * Закрыть фрейм с указанной командой
     * @param inst либо FrameItem либо экземпляр фрейма
     * @param cmd
     */
    closeFrame(inst, cmd) {
        let fi = this._resolveFrameItem(inst)
        if (!fi) {
            return // не наш фрейм
        }
        if (fi instanceof FrameItemDialog) {
            this._closeFrame_dialog(fi, cmd)
        } else {
            this._closeFrame_page(fi, cmd)
        }
    }

    _closeFrame_dialog(fi, cmd) {

        if (!cmd) {
            cmd = 'cancel'
        }
        let frameInst = fi.frameInst
        let handlerName = 'on' + upperFirst(cmd)

        function closeFrameProcess(eventsOwner, fnClose) {

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
                        fnClose()
                    })
                } else {
                    // можно закрывать
                    fnClose()
                }
            }

            if (jsaBase.isFunction(eventsOwner[handlerName])) {
                // есть обработчик onXxx
                handleProcess(eventsOwner[handlerName](frameInst, cmd))

            } else if (jsaBase.isFunction(eventsOwner.onCmd)) {
                handleProcess(eventsOwner.onCmd(frameInst, cmd))

            } else {
                // нет обработчиков
                fnClose()
            }
        }

        // поехали...

        // сначала события самого фпейма
        closeFrameProcess(frameInst, () => {
            // фрейм разрешил закрытся
            closeFrameProcess(fi.options, () => {
                // обработчики в опциях показа диалога разрешили закрытся
                fi.dialogInst.hideDialog()
            })
        })

    }

    _closeFrame_page(fi, cmd) {
        //todo пока это не нужно
    }

    //////

    /**
     * Алгоритм показа page
     * @param fi
     * @return {Promise<void>}
     * @private
     */
    async _showFrame_page(fi) {
        // сначала по быстрому монтируем фрейм
        // старый должен исчезнуть с экрана, но остался как экземпляр
        this._mountFrame(fi)

        // уничттожаем все старые
        while (this._frames_page.length > 0) {
            let fi = this._frames_page.pop()
            fi.destroy()
        }

        // сохраняем новый
        this._frames_page.push(fi)
    }

    /**
     * Алгоритм показа dialog
     * @param fi
     * @return {Promise<void>}
     * @private
     */
    async _showFrame_dialog(fi) {
        fi.dialogEl = jsaBase.dom.createTmpElement()
        let DialogCls = Vue.extend(Dialog)
        fi.dialogInst = new DialogCls({propsData: {frameInst: fi.frameInst}})
        fi.dialogInst.$on('dialog-close', () => {
            let a = this._frames_dialog.indexOf(fi)
            if (a !== -1) {
                this._frames_dialog.splice(a, 1)
            }
            fi.dialogInst.$destroy()
            fi.dialogInst = null
            fi.dialogEl.remove()
            fi.dialogEl = null
            fi.destroy()
        })
        fi.dialogInst.$mount(fi.dialogEl)
        this._frames_dialog.push(fi)
        fi.dialogInst.showDialog()
    }

    /**
     * Имеем FrameItem, нужно что бы у него стал точно известен frameCompCls
     * @param fi {FrameItem}
     */
    async _resolveFrameComp(fi) {
        if (fi.frameCompCls) {
            return
        }

        let comp = fi.frame

        if (!comp) {
            throw new Error("frame не указан для фрейма")
        }

        if (jsaBase.isString(comp)) {
            // заказана строка

            // пока просто считаем ее полным именем модуля
            await Jc.loadModule(comp)

            let mod = require(comp)
            comp = mod.default || mod
        }

        // теперь comp должен быть по идее компонентом
        fi.frameCompCls = Vue.extend(comp)
    }

    _getFramePlace() {
        let fp = this._framePlaces[this._framePlaces.length - 1]
        if (!fp) {
            throw new Error("Не определено место монтирования фрейма. Нужно использовать jc-place-frame в месте отображения фрейма")
        }
        return fp
    }

    _mountFrame(fi) {
        this._getFramePlace().mountFrame(fi)
    }

    _unmountFrame(fi) {
        this._getFramePlace().unmountFrame(fi)
    }

    /**
     * Для экземпляра FrameItem или фрейма возвращает FrameItem,
     * если этот фрейм сейчас показан либо в диалоге либо на как страница
     * @param inst
     * @return {null|*}
     * @private
     */
    _resolveFrameItem(inst) {
        for (let a of this._frames_dialog) {
            if (inst === a || inst === a.frameInst) {
                return a
            }
        }
        for (let a of this._frames_page) {
            if (inst === a || inst === a.frameInst) {
                return a
            }
        }
        return null
    }

}

/**
 * Обертка вокруг экземпляра фрейма
 */
export class FrameItem {

    /**
     * @param options параметры создания фрейма
     * @param options.frame фрейм. Может быть компонентом или строкой
     * @param options.propsData значения свойств, которые будут переданы фрейму при его
     *        создании
     */
    constructor(options) {
        // копия параметров
        this.options = jsaBase.extend({}, options)

        // свойства для фрейма
        this.propsData = jsaBase.extend({}, this.options.propsData)

        // параметры для фрейма
        this.params = jsaBase.extend({}, this.options.params)

        // заказанный фрейм: строка или компонент
        this.frame = this.options.frame

        // экземпляр фрейма
        this.frameInst = null

        // класс компонента фрейма
        this.frameCompCls = null

        // какой FrameManager управляет фреймом
        this.frameManager = null

        // удаляем не нужное
        delete this.options.propsData
        delete this.options.params
    }

    /**
     * Возвращает el фрейма
     */
    getEl() {
        return this.frameInst.$el
    }

    //////

    /**
     * Инициализация фрейма. Возможна интенсивная загрузка данных.
     * Вызов initFrame
     */
    async initFrame() {
        let initFrameArr = this.frameInst.$options['initFrame']
        if (initFrameArr) {
            for (let fn of initFrameArr) {
                await fn.call(this.frameInst)
            }
        }
    }

    /**
     * Уничтожить фрейм
     */
    destroy() {
        this.frameInst.$destroy()
        this.frameInst.frameManager = null
        this.frameInst = null
        this.frameCompCls = null
        this.frameManager = null
    }

}

export class FrameItemPage extends FrameItem {
}

export class FrameItemDialog extends FrameItem {
}

export async function showFrame(options) {
    let fi = new FrameItemPage(options)
    await jsaBase.app.frameManager.showFrame(fi)
}

export async function showDialog(options) {
    let fi = new FrameItemDialog(options)
    await jsaBase.app.frameManager.showFrame(fi)
}
