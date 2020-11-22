/*

Фреймы

----------------------------------------------------------------------------- */

import {jsaBase, Vue} from '../vendor'
import Dialog from './dialog/Dialog'
import upperFirst from 'lodash/upperFirst'
import {FrameRouter} from './router'
import {FrameHistory} from './history'

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
         * @member App#frameManager
         */
        this.app.frameManager = frameManager

        /**
         * Роутер для фреймов
         * @type {FrameRouter}
         * @member App#frameRouter
         */
        this.app.frameRouter = frameManager.frameRouter
    }


    onAfterRun() {
        let hash = jsaBase.url.getPageHash()
        let ri = this.app.frameRouter.resolve(hash)
        if (ri == null && hash !== '') {
            // если по текущему фрейму не удалось определить hash, то пробуем пустой,
            // т.е. переход на home
            hash = ''
            ri = this.app.frameRouter.resolve(hash)
        }
        if (ri != null) {
            this.app.frameManager.showFrame({
                frame: hash,
                __page__hash: hash, // hash, который привел к покаку фрейма, сохраняем
            })
        }
        //
        let th = this

        this._popstate = function(e) {
            if (jsaBase.cfg.envDev) {
                console.info("HISTORY POPSTATE", e);
            }
            th.app.frameManager.onPopstate(e)
        }
        window.addEventListener('popstate', this._popstate);
    }


    onStop() {
        if (this._popstate) {
            window.removeEventListener('popstate', this._popstate);
        }
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
        this.frameRouter = new FrameRouter()
        //
        this.history = new FrameHistory()
    }

    async showFrame(options) {
        let fw = new FrameWrapperPage(options)
        return await this.showFrameWrapper(fw)
    }

    async showDialog(options) {
        let fw = new FrameWrapperDialog(options)
        return await this.showFrameWrapper(fw)
    }

    /**
     * Показать фрейм
     * @param fw {FrameWrapper}
     */
    async showFrameWrapper(fw) {
        // метим своим
        fw.frameManager = this

        // делаем класс компонента
        await this._resolveFrameComp(fw)

        // создаем экземпляр
        fw.frameInst = new fw.frameCompCls({frameWrapper: fw})

        // инициализаируем (возможна интенсивная загрузка данных)
        await fw.initFrame()

        // теперь фрейм готов к работе
        fw.frameInst.$mount(jsaBase.dom.createTmpElement())

        // показываем его
        if (fw instanceof FrameWrapperDialog) {
            await this._showFrame_dialog(fw)
        } else {
            await this._showFrame_page(fw)
        }

        // меняем url, если допустимо
        let routePath = fw.getRoutePath()
        if (routePath != null) {
            this.history.updateHash(routePath)
        }

        //
        return fw.frameInst
    }

    //////

    registerPlaceFrame(inst) {
        if (Jc.cfg.envDev) {
            console.info("registerPlaceFrame", inst);
        }
        if (this._framePlaces.indexOf(inst) !== -1) {
            return // уже есть такой же
        }
        this._framePlaces.push(inst)
    }

    unregisterPlaceFrame(inst) {
        if (Jc.cfg.envDev) {
            console.info("unregisterPlaceFrame", inst);
        }
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

    async _closeFrame_dialog(fw, cmd) {

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

        if (jsaBase.isFunction(fw.options[handlerName])) {
            // обработчики в опциях показа диалога: обработчик onXxx
            if (await fw.options[handlerName](frameInst, cmd) === false) {
                return // закрываться нельзя
            }
        } else if (jsaBase.isFunction(fw.options.onCmd)) {
            // обработчики в опциях показа диалога: обработчик onCmd
            if (await fw.options.onCmd(frameInst, cmd) === false) {
                return // закрываться нельзя
            }
        }

        // все разрешили закрытся, закрываем
        fw.dialogInst.hideDialog()
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

        if (fw.frame == null) {
            throw new Error("frame не указан для фрейма")
        }

        if (jsaBase.isString(fw.frame)) {
            // заказана строка
            // возможно router знает про этот фрейм
            let routeInfo = this.frameRouter.resolve(fw.frame)
            if (routeInfo != null) {
                // да, знает
                fw.routeInfo = routeInfo
                // это фрейм
                fw.frame = routeInfo.frame
                // это параметры, объединяем с переданными, от route - важнее!
                fw.params = jsaBase.extend({}, fw.params, routeInfo.params)
                //
                if (fw.options.__page__hash) {
                    // фрейм пришел по настоянию адресной строки
                    fw.routeInfo.pageHash = fw.options.__page__hash
                    delete fw.options.__page__hash
                }
            }
        }

        let comp = fw.frame

        if (jsaBase.isFunction(comp)) {
            // это функция
            if (!comp.cid) { // маркер - это Vue-конструктор
                // функция не имеет маркера конструктора
                // вызываем ее, она должна вернуть компонент
                // или promise, который вернет компонент
                comp = await comp()
            }
        }

        if (jsaBase.isString(comp)) {
            // заказана строка, считаем ее полным именем модуля
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

    /**
     * Реакция на событие popstate
     * @param e
     */
    onPopstate(e) {
        let hash = jsaBase.url.getPageHash()
        let ri = this.frameRouter.resolve(hash)
        if (ri != null) {
            this.showFrame({frame: hash, __page__hash: hash})
        }
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
     * Возвращает path (вместе с параметрами), который можно использовать
     * в hash для ссылки на показ этого фрейма.
     * Возвращает null, если этот фрейм не доступен через router.
     */
    getRoutePath() {
        if (this.routeInfo == null) {
            return null
        }
        let res = this.routeInfo.path
        let prms = {}
        for (let p in this.params) {
            if (p in this.routeInfo.urlParams) {
                continue
            }
            let v = this.params[p]
            // берем только простые значения
            if (jsaBase.isString(v) || jsaBase.isNumber(v) || jsaBase.isBoolean(v)) {
                prms[p] = v
            }
        }
        let qs = jsaBase.url.params(prms)
        if (qs !== '') {
            res = res + '?' + qs
        }
        return res
    }

    /**
     * Уничтожить фрейм
     */
    destroy() {
        this.frameInst.$options.frameWrapper = null
        this.frameInst.$destroy()
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
    return await jsaBase.app.frameManager.showFrame(options)
}

export async function showDialog(options) {
    return await jsaBase.app.frameManager.showDialog(options)
}
