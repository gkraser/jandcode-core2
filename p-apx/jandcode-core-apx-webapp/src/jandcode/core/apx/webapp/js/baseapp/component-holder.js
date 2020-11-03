import {jsaBase, Vue} from '../vendor'
import lodashGet from 'lodash/get'
import lodashSet from 'lodash/set'

/**
 *  Хранилище компонентов.
 *  Используется для хранения и использования компонентов, привязанных к теме.
 */
export class ComponentHolder {

    constructor() {
        this.__items = {}
    }

    /**
     * Все компоненты
     */
    getAll() {
        return this.__items
    }

    /**
     * Добавить компонент
     * @param fullname полное имя (группа.имя)
     * @param Comp компонент
     */
    set(fullname, Comp) {
        lodashSet(this.__items, fullname, Vue.extend(Comp))
    }

    /**
     * Получить компонент
     * @param fullname полное имя (группа.имя)
     * @return компонент или группа, в зависимости от того,что передано в fullname.
     */
    get(fullname) {
        return lodashGet(this.__items, fullname)
    }

    /**
     * Получить группу с учетом темы
     * @param group группа
     * @return объединенный объект, где на компоненты в хранилище наложены
     *         компоненты из темы
     */
    getGroupWithTheme(group) {
        let res = {}
        let thisGroup = this.get(group)
        Object.assign(res, thisGroup)

        let themeGroup = lodashGet(Jc.cfg.theme.componentHolder, group)
        if (themeGroup) {
            for (let name in themeGroup) {
                let value = themeGroup[name]
                if (value) {
                    if (!jsaBase.isString(value)) {
                        console.warn("Jc.cfg.theme.components." + group + "." + name + ' is not String')
                    } else {
                        let comp = this.get(value)
                        if (comp && jsaBase.isFunction(comp)) {
                            res[name] = comp
                        }
                    }
                }
            }
        }

        return res
    }

    /**
     * Обновить состав components в экземпляре компонента из указанной
     * группы в соответствии с с темой
     * @param vueInst экземпляр (например фрейм)
     * @param group {String} группа (например 'frame')
     */
    updateComponents(vueInst, group) {
        let cmps = this.getGroupWithTheme(group)
        for (let nm in cmps) {
            vueInst.$options.components[nm] = cmps[nm]
        }
    }
}


