import BaseAction from './base-action'

export let config = {
    type: {
        primary: 'primary',
        success: 'success',
        danger: 'danger',
        warning: 'warning',
        info: 'info',
    }
}

export default {
    name: 'jc-button',

    mixins: [BaseAction],

    props: {
        type: {
            type: String
        }
    },

    render(h) {
        let css = ''
        if (this.type) {
            let s = config.type[this.type]
            if (s) {
                css = this.cn(s)
            }
        }
        return this.doRenderAction(h, css)
    },

}

