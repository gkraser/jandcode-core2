import {adaptCtxData} from '../vendor'

function isAttrTrue(v) {
    if (v == null || v === false) {
        return false
    }
    return true
}

export default {
    name: 'jc-btn',
    functional: true,
    render(h, ctx) {
        let data = adaptCtxData(ctx)
        data.class.push('jc-btn')
        data.attrs['no-caps'] = true
        data.attrs['unelevated'] = true

        // маркер обычной кнопки, для настройки цвета
        if (!isAttrTrue(ctx.props.flat) && !isAttrTrue(ctx.props.outline) && !ctx.props.color) {
            data.class.push('jc-btn--normal')
        }
        //

        return h('q-btn', ctx.data, ctx.children)
    }
}