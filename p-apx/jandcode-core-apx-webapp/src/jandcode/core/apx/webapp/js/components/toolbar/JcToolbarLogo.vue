<template>
    <div :class="classes" v-on="$listeners">
        <q-icon :name="icon"/>
    </div>
</template>

<script>
export default {
    name: 'jc-toolbar-logo',
    props: {
        icon: {},

        /**
         * Размер (md, sm....)
         */
        size: {}

    },
    data() {
        return {}
    },
    computed: {
        classes() {
            let res = ['jc-toolbar-logo']
            if (this.size) {
                res.push('jc-toolbar-logo--' + this.size)
            }
            if (this.$listeners.click) {
                res.push('cursor-pointer')
            }
            return res
        }
    },
    methods: {},
}
</script>
