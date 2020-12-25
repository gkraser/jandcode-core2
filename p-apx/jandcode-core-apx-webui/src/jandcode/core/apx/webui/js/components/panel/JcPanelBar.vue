<template>
    <div :class="classes">
        <jc-toolbar>
            <template v-if="hasTitle">
                <jc-toolbar-title :text="title"/>
                <q-space/>
            </template>
            <slot name="default">
            </slot>
        </jc-toolbar>
    </div>
</template>

<script>
export default {
    name: 'jc-panel-bar',
    props: {
        title: {},
    },
    computed: {
        classes() {
            let res = ['jc-panel__bar']
            return res
        },
        hasTitle() {
            return !!this.title
        }
    },
}
</script>
