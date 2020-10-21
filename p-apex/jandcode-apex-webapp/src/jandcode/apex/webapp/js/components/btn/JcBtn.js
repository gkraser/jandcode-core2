import {jsaVue} from '../vendor'

function isAttrTrue(v) {
    if (v == null || v === false) {
        return false
    }
    return true
}

export let config = {
    color: {
        normal: {
            color: 'grey-3',
            textColor: 'black',
        },
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
        let data = jsaVue.adaptCtxData(ctx)

        data.class.push('jc-btn')

        if (data.attrs['no-caps'] == null) {
            data.attrs['no-caps'] = true
        }
        if (data.attrs['no-wrap'] == null) {
            data.attrs['no-wrap'] = true
        }
        if (data.attrs['unelevated'] == null) {
            data.attrs['unelevated'] = true
        }

        let color = ctx.props.color || 'normal'
        let colorDef
        colorDef = config.color[color] || config.color['normal']
        data.attrs['color'] = colorDef.color
        data.attrs['textColor'] = colorDef.textColor

        //
        if (!isAttrTrue(ctx.props.flat) &&
            !isAttrTrue(ctx.props.outline) &&
            !isAttrTrue(ctx.props.round)) {
            // обычная кнопка, у меня - с рамкой
            data.class.push('jc-btn--normal')
            data.class.push('jc-btn--' + color)
            //
            data.attrs['padding'] = 'sm'
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