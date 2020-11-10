<template>
    <div :class="classes" v-on="$listeners">
        <div class="jc-toolbar-title--text">{{ text }}</div>
        <template v-if="text2">
            <div class="jc-toolbar-title--text2">{{ text2 }}</div>
        </template>
    </div>
</template>

<script>
let nm = 'jc-toolbar-title'

export default {
    name: nm,
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
            let res = [nm]
            if (this.size) {
                res.push(nm + '--' + this.size)
            }
            if (this.text2) {
                res.push(nm + '--with-text2')
            }
            if (this.$listeners.click) {
                res.push('cursor-pointer')
            }
            return res
        }
    },
}
</script>
