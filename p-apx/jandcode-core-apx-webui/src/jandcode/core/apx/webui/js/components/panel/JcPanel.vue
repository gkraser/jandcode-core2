<template>
    <div :class="classes">
        <template v-if="hasPanelParts()">
            <slot name="default">
            </slot>
        </template>
        <template v-else>
            <template v-if="hasHeader">
                <jc-panel-bar :title="title">
                    <slot name="toolbar">
                    </slot>
                </jc-panel-bar>
            </template>
            <jc-panel-body :body-fit="bodyFit" :no-padding="noPadding">
                <slot name="default">
                </slot>
            </jc-panel-body>
            <template v-if="$slots.footer">
                <jc-panel-bar>
                    <slot name="footer">
                    </slot>
                </jc-panel-bar>
            </template>
        </template>
    </div>
</template>

<script>
import {jsaVue} from '../vendor'

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
        hasHeader() {
            return !!this.title || !!this.$slots.toolbar;
        }
    },
    methods: {
        /**
         * Есть ли явно определенные части панели
         */
        hasPanelParts() {
            return jsaVue.hasChild(this, (name) => {
                return name.startsWith('jc-panel-')
            })
        }
    }
}
</script>
