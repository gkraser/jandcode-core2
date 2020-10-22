import {jsaVue} from '../vendor'

export default {
    name: 'tst-btn',
    functional: true,
    render(h, ctx) {
        let data = jsaVue.adaptCtxData(ctx)

        data.attrs['no-caps'] = true
        data.attrs['no-wrap'] = true
        data.attrs['unelevated'] = true
        data.attrs['color'] = ctx.props.color || 'primary'

        return h('q-btn', ctx.data, ctx.children)
    }
}