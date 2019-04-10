import {jsaBase} from '../vendor'

let bc = 'jc-icon'

export default {
    name: bc,
    functional: true,

    props: {
        icon: String,
        url: String,
    },

    render(h, ctx) {
        if (ctx.props.url) {
            let url = jsaBase.url.ref(ctx.props.url)
            return h("div", {
                staticClass: bc,
                style: ctx.data.style,
                'class': ctx.data.class,
                attrs: ctx.data.attrs,
                on: ctx.listeners
            }, [
                h("img", {attrs: {src: url}})
            ])
        } else {
            let iconId = jsaBase.svgicon.iconId(ctx.props.icon)
            return h("div", {
                staticClass: bc,
                style: ctx.data.style,
                'class': ctx.data.class,
                attrs: ctx.data.attrs,
                on: ctx.listeners
            }, [
                h("svg", [h("use", {attrs: {"xlink:href": '#' + iconId}})])
            ])
        }
    }

}                