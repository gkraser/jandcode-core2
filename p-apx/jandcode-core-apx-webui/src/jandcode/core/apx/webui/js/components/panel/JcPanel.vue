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
        <q-card-section :class="classesBody">
            <slot name="default">
            </slot>
        </q-card-section>
        <template v-if="$slots.footer">
            <q-card-section class="jc-panel__footer">
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
        title: {},
        bodyFit: {
            type: Boolean
        },
        noPadding: {
            type: Boolean
        }
    },
    computed: {
        classes() {
            let res = ['jc-panel']
            return res
        },
        classesBody() {
            let res = ['jc-panel__body']
            if (this.bodyFit) {
                res.push('jc-panel__body--fit')
            }
            if (!this.noPadding) {
                res.push('jc-panel__body--padding')
            }
            return res
        },
        hasHeader() {
            return !!this.title || !!this.$slots.toolbar;
        }
    },
}
</script>
