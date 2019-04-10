export default {
    name: 'jc-list',

    props: {

        /**
         * true - скрыть место под toggle. На усмотрение компонента jc-list-item
         */
        hideToggle: {
            type: Boolean,
        },

        /**
         * true - скрыть место под иконку. На усмотрение компонента jc-list-item#content
         */
        hideIcon: {
            type: Boolean,
        }

    },

    data() {
        return {}
    },

    render(h) {
        let cls = this.cn()
        return h('div', {'class': cls}, this.$slots['default'])
    },


    computed: {
        // hasNested() {
        //     let z = this.$slots['default']
        //     if (!z) {
        //         return false
        //     }
        //     for (let i = 0; i < z.length; i++) {
        //         if (z[i].data && z[i].data.scopedSlots && z[i].data.scopedSlots.nested) {
        //             return true
        //         }
        //     }
        //     return false
        // }
    },

    methods: {}
}
