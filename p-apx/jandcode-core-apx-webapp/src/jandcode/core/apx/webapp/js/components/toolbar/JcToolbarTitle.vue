<template>
    <div :class="classes" v-on="$listeners">
        <div class="jc-toolbar-title__text">{{ text }}</div>
        <template v-if="text2">
            <div class="jc-toolbar-title__text2">{{ text2 }}</div>
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
    },
    data() {
        return {}
    },
    computed: {
        classes() {
            let res = [nm]
            if (this.text2) {
                res.push(nm + '--has-text2')
            }
            if (this.$listeners.click) {
                res.push('cursor-pointer')
            }
            return res
        }
    },
}
</script>
