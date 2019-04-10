import BaseAction from './base-action'

export default {
    name: 'jc-action',

    mixins: [BaseAction],

    render(h) {
        return this.doRenderAction(h)
    },

}

