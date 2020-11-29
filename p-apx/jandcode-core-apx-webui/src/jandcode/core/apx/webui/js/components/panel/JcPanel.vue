<template>
    <q-card :class="classes">
        <template v-if="hasHeader">
            <q-card-section>
                <jc-toolbar>
                    <jc-toolbar-title :text="title"
                                      class="text-size-large-1 text-weight-medium"/>
                    <q-space/>
                    <slot name="toolbar">
                    </slot>
                </jc-toolbar>
            </q-card-section>
        </template>
        <template v-if="$slots.body">
            <slot name="body">
            </slot>
        </template>
        <template v-else>
            <q-card-section>
                <slot name="default">
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
