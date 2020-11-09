<template>
    <div :class="classes" v-on="$listeners">
        <template v-if="text2">
            <div class="jc-toolbar-title--text1">{{ text }}</div>
            <div class="jc-toolbar-title--text2">{{ text2 }}</div>
        </template>
        <div v-else class="jc-toolbar-title--text">{{ text }}</div>
    </div>
</template>

<script>
export default {
    name: 'jc-toolbar-title',
    props: {
        text: {},
        text2: {},

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
            let res = ['jc-toolbar-title']
            console.info("this.size",this.size);
            if (this.size) {
                res.push('jc-toolbar-title--' + this.size)
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
