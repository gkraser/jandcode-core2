/*

Фреймы

----------------------------------------------------------------------------- */

import {jsaBase, Vue} from '../vendor'
import Dialog from './dialog/Dialog'
import upperFirst from 'lodash/upperFirst'
import {FrameRouter} from './router'

// опция initFrame будет выглядеть как массив (аналогично другим life-cycle hookd)
Vue.config.optionMergeStrategies.initFrame = Vue.config.optionMergeStrategies.created

/**
 * Сервис для менеджера фреймов
 */
export class FrameManagerService extends jsaBase.AppService {

    onCreate() {
        let frameManager = new FrameManager()

        /**
         * Текущий менеджер фреймов
         * @type {FrameManager}
         * @name frameManager
         * @memberOf jsaBase.app
         */
        this.app.frameManager = frameManager

        /**
         * Роутер для фреймов
         * @type {FrameRouter}
         */
        this.app.router = frameManager.router

    }

}

export class FrameManager {

    constructor() {
        // места монтирования фреймов
        this._framePlaces = []
        // все текущие работающие фреймы
        this._frames_page = []
        this._frames_dialog = []
        // router
        this.router = new FrameRouter()
    }

    /**
     * Показать фрейм
     * @param fw {FrameWrapper}
     */
    async showFrame(fw) {
        // метим своим
        fw.frameManager = this

        // делаем класс компонента
        await this._resolveFrameComp(fw)

        // создаем экземпляр
        fw.frameInst = new fw.frameCompCls({frameWrapper: fw})

        // инициализаируем (возможна интенсивная загрузка данных)
        await fw.initFrame()

        // теперь фрейм готов к работе
        fw.frameInst.$mount()

        // показываем его
        if (fw instanceof FrameWrapperDialog) {
            await this._showFrame_dialog(fw)
        } else {
            await this._showFrame_page(fw)
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
     * @param inst либо FrameWrapper либо экземпляр фрейма
     * @param cmd
     */
    closeFrame(inst, cmd) {
        let fw = this._resolveFrameWrapper(inst)
        if (!fw) {
            return // не наш фрейм
        }
        if (fw instanceof FrameWrapperDialog) {
            this._closeFrame_dialog(fw, cmd)
        } else {
            this._closeFrame_page(fw, cmd)
        }
    }

    _closeFrame_dialog(fw, cmd) {

        if (!cmd) {
            cmd = 'cancel'
        }
        let frameInst = fw.frameInst
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
            closeFrameProcess(fw.options, () => {
                // обработчики в опциях показа диалога разрешили закрытся
                fw.dialogInst.hideDialog()
            })
        })

    }

    _closeFrame_page(fw, cmd) {
        //todo пока это не нужно
    }

    //////

    /**
     * Алгоритм показа page
     * @param fw
     * @return {Promise<void>}
     * @private
     */
    async _showFrame_page(fw) {
        // сначала по быстрому монтируем фрейм
        // старый должен исчезнуть с экрана, но остался как экземпляр
        this._mountFrame(fw)

        // уничттожаем все старые
        while (this._frames_page.length > 0) {
            let fw = this._frames_page.pop()
            fw.destroy()
        }

        // сохраняем новый
        this._frames_page.push(fw)
    }

    /**
     * Алгоритм показа dialog
     * @param fw
     * @return {Promise<void>}
     * @private
     */
    async _showFrame_dialog(fw) {
        fw.dialogEl = jsaBase.dom.createTmpElement()
        let DialogCls = Vue.extend(Dialog)
        fw.dialogInst = new DialogCls({propsData: {frameInst: fw.frameInst}})
        fw.dialogInst.$on('dialog-close', () => {
            let a = this._frames_dialog.indexOf(fw)
            if (a !== -1) {
                this._frames_dialog.splice(a, 1)
            }
            fw.dialogInst.$destroy()
            fw.dialogInst = null
            fw.dialogEl.remove()
            fw.dialogEl = null
            fw.destroy()
        })
        fw.dialogInst.$mount(fw.dialogEl)
        this._frames_dialog.push(fw)
        fw.dialogInst.showDialog()
    }

    /**
     * Имеем FrameWrapper, нужно что бы у него стал точно известен frameCompCls
     * @param fw {FrameWrapper}
     */
    async _resolveFrameComp(fw) {
        if (fw.frameCompCls) {
            return
        }

        if (!fw.frame) {
            throw new Error("frame не указан для фрейма")
        }

        if (jsaBase.isString(fw.frame)) {
            // заказана строка
            // возможно router знает про этот фрейм
            let routeInfo = this.router.resolve(fw.frame)
            if (routeInfo != null) {
                // да, знает
                fw.routeInfo = routeInfo
                // это фрейм
                fw.frame = routeInfo.frame
                // это параметры, объединяем с переданными, перекрывая от route
                fw.params = jsaBase.extend({}, routeInfo.params, fw.params)
            }
        }

        let comp = fw.frame
        if (jsaBase.isString(comp)) {
            // заказана строка
            // пока просто считаем ее полным именем модуля
            await Jc.loadModule(comp)

            let mod = require(comp)
            comp = mod.default || mod
        }

        // теперь comp должен быть по идее компонентом
        fw.frameCompCls = Vue.extend(comp)
    }

    _getFramePlace() {
        let fp = this._framePlaces[this._framePlaces.length - 1]
        if (!fp) {
            throw new Error("Не определено место монтирования фрейма. Нужно использовать jc-place-frame в месте отображения фрейма")
        }
        return fp
    }

    _mountFrame(fw) {
        this._getFramePlace().mountFrame(fw)
    }

    _unmountFrame(fw) {
        this._getFramePlace().unmountFrame(fw)
    }

    /**
     * Для экземпляра FrameWrapper или фрейма возвращает FrameWrapper,
     * если этот фрейм сейчас показан либо в диалоге либо на как страница
     * @param inst
     * @return {null|*}
     * @private
     */
    _resolveFrameWrapper(inst) {
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
export class FrameWrapper {

    /**
     * @param options параметры создания фрейма
     * @param options.frame фрейм. Может быть компонентом или строкой
     * @param options.params произвольные параметры
     */
    constructor(options) {
        // копия параметров
        this.options = jsaBase.extend({}, options)

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

        // если фрейм заресолвился через router, тут информация
        this.routeInfo = null

        // удаляем не нужное
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
     * Закрыть фрейм с указанной командой.
     * Используется в диалогах.
     * @param cmd
     */
    closeFrame(cmd) {
        this.frameManager.closeFrame(this, cmd)
    }

    /**
     * Уничтожить фрейм
     */
    destroy() {
        this.frameInst.$options.frameWrapper = null
        this.frameInst.$destroy()
        this.frameInst.frameManager = null
        this.frameInst = null
        this.frameCompCls = null
        this.frameManager = null
    }

}

export class FrameWrapperPage extends FrameWrapper {
}

export class FrameWrapperDialog extends FrameWrapper {
}

export async function showFrame(options) {
    let fw = new FrameWrapperPage(options)
    await jsaBase.app.frameManager.showFrame(fw)
}

export async function showDialog(options) {
    let fw = new FrameWrapperDialog(options)
    await jsaBase.app.frameManager.showFrame(fw)
}
