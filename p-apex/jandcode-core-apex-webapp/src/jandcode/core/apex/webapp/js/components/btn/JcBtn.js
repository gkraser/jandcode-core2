import {jsaVue} from '../vendor'

function isAttrTrue(v) {
    if (v == null || v === false) {
        return false
    }
    return true
}

export let config = {
    kind: {
        default: {},
        danger: {},
    },
}

let sizes = {
    xs: 'xs',
    sm: 'sm',
    md: 'sm',
    lg: 'lg',
    xl: 'xl'
}

export default {
    name: 'jc-btn',
    functional: true,
    render(h, ctx) {
        let data = jsaVue.adaptCtxData(ctx)

        data.class.push('jc-btn')

        if (data.attrs['no-wrap'] == null) {
            data.attrs['no-wrap'] = true
        }

        // размер задаем через классы
        if ('size' in data.attrs) {
            let sz = data.attrs['size']
            if (sz in sizes) {
                data.class.push('font-size-' + sz)
                delete data.attrs['size']
            }
        }

        let kind = ctx.props.kind || 'default'

        //
        if (!isAttrTrue(ctx.props.flat) &&
            !isAttrTrue(ctx.props.outline) &&
            !isAttrTrue(ctx.props.round)) {
            // обычная кнопка
            data.class.push('jc-btn--default')
            if (kind !== 'default') {
                data.class.push('jc-btn--' + kind)
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