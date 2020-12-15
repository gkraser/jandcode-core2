<template>
    <div :class="classes">
        <template v-if="hasHeader">
            <div class="jc-panel__header">
                <jc-toolbar>
                    <slot name="header">
                        <jc-toolbar-title :text="title"/>
                        <q-space/>
                        <slot name="toolbar">
                        </slot>
                    </slot>
                </jc-toolbar>
            </div>
        </template>
        <div :class="classesBody">
            <slot name="default">
            </slot>
        </div>
        <template v-if="$slots.footer">
            <div class="jc-panel__footer">
                <jc-toolbar>
                    <slot name="footer">
                    </slot>
                </jc-toolbar>
            </div>
        </template>
    </div>
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
            return !!this.title || !!this.$slots.toolbar || !!this.$slots.header;
        }
    },
}
</script>
