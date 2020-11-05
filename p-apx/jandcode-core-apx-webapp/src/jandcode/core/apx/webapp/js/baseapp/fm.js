/*

Фреймы

----------------------------------------------------------------------------- */

import {jsaBase, Vue} from '../vendor'

export class FrameManager {

    constructor() {
        // места монтирования фреймов
        this._framePlaces = []
        // все текущие работающие фреймы
        this._frames = []
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

        // показываем его
        await this._showFrame(fi)

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
     * Собственно алгоритм показа
     * @param fi
     * @return {Promise<void>}
     * @private
     */
    async _showFrame(fi) {
        // сначала по быстрому монтируем фрейм
        // старый должен исчезнуть с экрана, но остался как экземпляр
        this._mountFrame(fi)

        // уничттожаем все старые
        while (this._frames.length > 0) {
            let fi = this._frames.pop()
            fi.destroy()
        }

        // сохраняем новый
        this._frames.push(fi)
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
        this.frameInst = null
        this.frameCompCls = null
    }

}

/**
 * Глобальный экземпляр менеджера фреймов
 * @type {FrameManager}
 */
export let frameManager = new FrameManager()

export async function showFrame(options) {
    let fi = new FrameItem(options)
    await frameManager.showFrame(fi)
}
