import {adaptCtxData} from '../vendor'

function isAttrTrue(v) {
    if (v == null || v === false) {
        return false
    }
    return true
}


export let config = {
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
        //todo только если не усановлено явно
        data.attrs['no-caps'] = true
        data.attrs['no-wrap'] = true
        data.attrs['unelevated'] = true

        //
        if (!isAttrTrue(ctx.props.flat) && !isAttrTrue(ctx.props.outline)) {
            // обычная кнопка, у меня - с рамкой
            data.class.push('jc-btn--normal')
            let color = ctx.props.color
            if (color) {
                let colorDef = config.color[color]
                if (colorDef) {
                    data.attrs['color'] = colorDef.color
                    data.attrs['textColor'] = colorDef.textColor
                    data.class.push('jc-btn--' + color)
                }
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