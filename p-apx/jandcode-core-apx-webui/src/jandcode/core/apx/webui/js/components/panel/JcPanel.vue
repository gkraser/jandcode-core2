<template>
    <q-card :class="classes">
        <template v-if="hasHeader">
            <q-card-section class="jc-panel__header">
                <jc-toolbar>
                    <jc-toolbar-title :text="title"/>
                    <q-space/>
                    <slot name="toolbar">
                    </slot>
                </jc-toolbar>
            </q-card-section>
        </template>
        <q-card-section class="jc-panel__body">
            <slot name="default">
            </slot>
        </q-card-section>
        <template v-if="$slots.footer">
            <q-card-section class="jc-panel_footer">
                <slot name="footer">
                </slot>
            </q-card-section>
        </template>
    </q-card>
</template>

<script>
export default {
    name: 'jc-panel',
    props: {
        title: {}
    },
    computed: {
        classes() {
            return ['jc-panel']
        },
        hasHeader() {
            return !!this.title || !!this.$slots.toolbar;
        }
    },
}
</script>
