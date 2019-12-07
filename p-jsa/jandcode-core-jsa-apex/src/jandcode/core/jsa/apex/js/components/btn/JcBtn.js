import {adaptCtxData} from '../vendor'

function isAttrTrue(v) {
    if (v == null || v === false) {
        return false
    }
    return true
}

/*

    Дополнительные свойства:

    kind: String


 */

let config = {
    kind: {
        primary: 'primary',
        success: 'success',
        danger: 'danger',
        warning: 'warning',
        info: 'info',
    },
    color: {
        primary: {
            color: 'primary',
            textColor: 'white',
        },
        success: {
            color: 'positive',
            textColor: 'white',
        },
        danger: {
            color: 'negative',
            textColor: 'white',
        },
        warning: {
            color: 'warning',
            textColor: 'white',
        },
        info: {
            color: 'info',
            textColor: 'white',
        },
    }
}

export default {
    name: 'jc-btn',
    functional: true,
    render(h, ctx) {
        let data = adaptCtxData(ctx)
        data.class.push('jc-btn')
        data.attrs['no-caps'] = true
        data.attrs['no-wrap'] = true
        data.attrs['unelevated'] = true

        // маркер обычной кнопки, для настройки цвета
        if (!isAttrTrue(ctx.props.flat) && !isAttrTrue(ctx.props.outline) && !ctx.props.color) {
            data.class.push('jc-btn--normal')
            if (ctx.props.kind) {
                data.class.push('jc-btn--' + ctx.props.kind)
            }
        }

        // если тип явно не установлен, и нет click - ставим 'a'
        if (!data.attrs.type) {
            if (!(ctx.listeners.click)) {
                data.attrs.type = 'a'
            }
        }

        return h('q-btn', ctx.data, ctx.children)
    }
}