/*

Базовые утилиты

----------------------------------------------------------------------------- */

function _isCompCheckType(name, type) {
    return 'jc-' + type === name;
}

/**
 * Проверка, что компонент является опрееделенным компонентом
 * @param comp экземпляр компонента или VNode
 * @param type тип. Для компонентов - без префикса 'jc-'.
 */
export function isComp(comp, type) {
    if (!comp) {
        return false;
    }
    if (comp.$options) {
        // это экземпляр компонента
        return _isCompCheckType(comp.$options.name, type);
    }

    if (comp.componentOptions) {
        // это VNode

        let opt = comp.componentOptions;

        if (opt.Ctor && opt.Ctor.options) {
            return _isCompCheckType(opt.Ctor.options.name, type)
        }

        return _isCompCheckType(opt.tag, type)
    }

    return false;
}


/**
 * Ищет компонент с указанным типом, которому принадлежит компонент from.
 * Возвращает найденный компонент или null, если не найден.
 * @param from с какого компонента искать
 * @param type тип искомого компонента
 */
export function findCompUp(from, type) {
    if (!from) return null;
    if (isComp(from, type)) return from;
    if (from.$parent) return findCompUp(from.$parent, type)
    return null;
}

/**
 * Ищет самый верхний компонент с указанным типом, которому принадлежит компонент from.
 * Возвращает найденный компонент или null, если не найден.
 * @param from с какого компонента искать
 * @param type тип искомого компонента
 */
export function findCompTop(from, type) {
    let res = null
    let cur = from
    while (cur) {
        if (isComp(cur, type)) {
            res = cur;
        }
        cur = cur.$parent;
    }
    return res;
}

/**
 * Возвращает dom-элемент
 * @param from откуда брать. Может быть компонентом или элементом
 */
export function getEl(from) {
    if (!from) return null;
    if (from instanceof Element) return from;
    if (from.$el instanceof Element) return from.$el;
    return null;
}
