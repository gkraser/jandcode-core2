/*

Построитель меню по json

----------------------------------------------------------------------------- */

import {jsaBase} from '../vendor'

export default {
    name: 'jc-menu-builder',

    functional: true,

    props: {

        /**
         * Список элементов
         */
        items: {
            type: Array
        },

        /**
         * Имя свойства с меткой меню
         */
        labelProp: {
            default: 'label'
        },

        /**
         * Компонент для пунктов меню.
         */
        itemComp: {
            default: 'jc-side-menu-item'
        },

        /**
         * Иконка по умолчанию, если для пунктя явно не установлена.
         * По умолчанию - null (не ставить иконку по умолчанию)
         */
        iconDefault: {
            default: null,
        }
    },

    render(h, ctx) {
        let {
            items,
            labelProp,
            itemComp,
            iconDefault
        } = ctx.props

        function step(item) {
            let childs = []
            let on = {}

            let folder = item.items && jsaBase.isArray(item.items)
            if (folder) {
                for (let child of item.items) {
                    childs.push(step(child))
                }
            }


            let props = {
                label: item[labelProp] || item.label,
            }

            let icon = item.icon
            if (!icon) {
                if (iconDefault) {
                    icon = iconDefault
                }
            }
            if (icon) {
                props.icon = icon
            }

            if (item.frame) {
                props.frame = item.frame
            }

            if (folder) {
                props.opened = !!item.opened
            }

            return h(itemComp, {
                props: props,
                on: on,
            }, childs)
        }

        let res = []
        if (jsaBase.isArray(items)) {
            for (let child of items) {
                res.push(step(child))
            }
        }
        return res
    }
}

