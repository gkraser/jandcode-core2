import {jsaVue} from '../vendor'

export default {
    name: 'tst-checkbox',
    functional: true,
    render(h, ctx) {
        let data = jsaVue.adaptCtxData(ctx)

        //data.attrs['size'] = 'xs'
        data.attrs['dense'] = true

        return h('q-checkbox', ctx.data, ctx.children)
    }
}