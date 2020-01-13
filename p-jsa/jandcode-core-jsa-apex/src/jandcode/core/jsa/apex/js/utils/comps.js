/*

Утилиты для компонентов

----------------------------------------------------------------------------- */

import {Vue, jsaBase} from '../vendor'

function _isCompCheckType(name, type) {
    return type === name;
}

/**
 * Проверка, что компонент является опрееделенным компонентом
 * @param comp экземпляр компонента или VNode
 * @param type тип
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
 * @param type тип искомого компонента (включая его самого)
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

/**
 * Регистрация компонентов
 * @param comps набор компонентов. Каждый компонент, имеющий name будет зарегистрирован
 */
export function registerComponents(comps) {
    for (let key in comps) {
        let comp = comps[key];
        if (comp.name) {
            Vue.component(comp.name, comp)
        }
    }
}

function adaptCtxClasses(ctx) {
    let ar = ctx.data.class
    if (jsaBase.isArray(ctx.data.class)) {
        return ctx.data.class
    }
    if (ctx.data.class == null) {
        ctx.data.class = []
    } else {
        ctx.data.class = [ctx.data.class]
    }
    return ctx.data.class
}

function adaptCtxAttrs(ctx) {
    if (ctx.data.attrs == null) {
        ctx.data.attrs = {}
    }
    return ctx.data.attrs
}

/**
 * Адаптировать ctx функционального компонента для модификации
 * @param ctx контекст функционального компонента
 * @return {*} свойство ctx.data
 */
export function adaptCtxData(ctx) {
    adaptCtxAttrs(ctx)
    adaptCtxClasses(ctx)
    return ctx.data
}
