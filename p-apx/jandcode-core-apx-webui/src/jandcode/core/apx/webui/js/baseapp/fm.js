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
        // зарегистрированные shower
        this._showers = {}
        // router
        this.frameRouter = new FrameRouter()
        //
        this.history = new FrameHistory()
        //
        this._showers['dialog'] = new FrameShower_dialog()
    }

    /**
     * Показать фрейм
     * @param options конфигурация фрейма
     * @param options.frame фрейм. Компонент, имя модуля со фреймом, route-path
     * @param options.params {Object} параметры фрейма
     * @param options.shower {Object} какой shower использовать. По умолчанию - main
     * @return {Promise<null>}
     */
    async showFrame(options) {
        let fw = new FrameWrapper(options)
        return await this.showFrameWrapper(fw)
    }

    /**
     * Показать фрейм
     * @param fw {FrameWrapper}
     */
    async showFrameWrapper(fw) {
        // метим своим
        fw.frameManager = this

        // определяем shower
        if (!fw.shower) {
            fw.shower = 'main'
        }
        if (jsaBase.isString(fw.shower)) {
            let shower = this._showers[fw.shower]
            if (!shower) {
                if (fw.shower === 'main') {
                    throw new Error("Не определен shower 'main'. Нужно использовать компонент 'jc-shower-main' в месте отображения фрейма")
                } else {
                    throw new Error("Указан не существующий shower: " + fw.shower)
                }
            }
            fw.shower = shower
        }

        // делаем класс компонента
        await this._resolveFrameComp(fw)

        // создаем экземпляр
        fw.frameInst = new fw.frameCompCls({frameWrapper: fw})

        // инициализаируем (возможна интенсивная загрузка данных)
        await fw.initFrame()

        // теперь фрейм готов к работе
        fw.frameInst.$mount(jsaBase.dom.createTmpElement())

        // показываем его
        await fw.shower.showFrameWrapper(fw)

        // меняем url, если допустимо  //todo видимо в shower main!
        let routePath = fw.getRoutePath()
        if (routePath != null) {
            this.history.updateHash(routePath)
        }

        //
        return fw.frameInst
    }

    //////

    /**
     * Зарегистрировать shower
     * @param name имя
     * @param shower экземпляр
     */
    registerShower(name, shower) {
        this._showers[name] = shower
    }

    /**
     * Отменить регистрацию shower
     * @param shower ранее зарегистророванный экземпляр
     */
    unregisterShower(shower) {
        //todo unregisterShower
    }

    /**
     * Зарегистрированные shower
     * @return {Object} key: имя shower, value: экземпляр shower
     */
    getShowers() {
        return this._showers
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
        if (!fw.shower) {
            return // не показан
        }
        fw.shower.closeFrameWrapper(fw, cmd)
    }

    //////

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

    /**
     * Для экземпляра FrameWrapper или фрейма возвращает FrameWrapper,
     * если этот фрейм сейчас показан либо в диалоге либо на как страница
     * @param inst
     * @return {null|*}
     * @private
     */
    _resolveFrameWrapper(inst) {
        if (inst instanceof FrameWrapper) {
            return inst
        }
        if ('$options' in inst) {
            let fw = inst.$options.frameWrapper
            if (fw instanceof FrameWrapper) {
                return fw
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

        // кто показывает фрейм
        this.shower = this.options.shower

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
        this.shower = null
    }

}

//////

/**
 * Показывальщик фреймов
 */
export class FrameShower {

    constructor() {
        // все мои текущие работающие фреймы
        this._frames = []
    }

    /**
     * Показать указанный FrameWrapper.
     * Он уже прошел инициализацию и монтирование во временный элемент.
     * @param fw {FrameWrapper} что показать
     * @return {Promise<void>}
     */
    async showFrameWrapper(fw) {
        throw new Error("Not implemented showFrameWrapper")
    }

    /**
     * Закрыть фрейм с указанной командой
     * @param fw {FrameWrapper} что закрывать. Гарантированно принадлежит этому shower
     * @param cmd
     */
    closeFrameWrapper(fw, cmd) {
        throw new Error("Not implemented closeFrameWrapper")
    }

    /**
     * Уничтожить shower
     */
    destroy() {
    }

}

/**
 * Стандартный shower для показа диалогов
 */
export class FrameShower_dialog extends FrameShower {

    async showFrameWrapper(fw) {
        fw.dialogEl = jsaBase.dom.createTmpElement()
        let DialogCls = Vue.extend(Dialog)
        fw.dialogInst = new DialogCls({propsData: {frameInst: fw.frameInst}})
        fw.dialogInst.$on('dialog-close', () => {
            let a = this._frames.indexOf(fw)
            if (a !== -1) {
                this._frames.splice(a, 1)
            }
            fw.dialogInst.$destroy()
            fw.dialogInst = null
            fw.dialogEl.remove()
            fw.dialogEl = null
            fw.destroy()
        })
        fw.dialogInst.$mount(fw.dialogEl)
        this._frames.push(fw)
        fw.dialogInst.showDialog()
    }

    async closeFrameWrapper(fw, cmd) {

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

}

//////

export async function showFrame(options) {
    return await jsaBase.app.frameManager.showFrame(options)
}

export async function showDialog(options) {
    options = jsaBase.extend(options, {shower: 'dialog'})
    return await jsaBase.app.frameManager.showFrame(options)
}
