<template>
    <jc-btn :class="classes" :flat="inToolbar || isMenuItem"
            align="left"
            v-on="$listeners" v-close-popup="isMenuItem && !hasSubMenu"
            :label="label" :icon="leftIcon" :icon-right="rightIcon"
            v-bind="$attrs">
        <template v-if="hasSubMenu">
            <q-menu content-class="jc-action--menu"
                    :anchor="isMenuItem?'top right':null"
                    square>
                <div class="column">
                    <slot name="default"></slot>
                </div>
            </q-menu>
        </template>
    </jc-btn>
</template>

<script>
    export default {
        name: 'jc-action',
        props: {
            label: {},
            icon: {default: null},
        },
        provide() {
            return {
                isMenuItem: true
            }
        },
        inject: {
            isMenuItem: {default: false},
            inToolbar: {default: false},
        },
        computed: {
            classes() {
                let res = ['jc-action']
                if (this.isMenuItem) {
                    res.push('jc-action--menuitem')
                }
                if (this.hasSubMenu) {
                    res.push('jc-action--dropdown')
                }
                return res
            },
            leftIcon() {
                let res = this.icon
                if (!res && this.isMenuItem) {
                    res = 'empty'
                }
                return res
            },
            rightIcon() {
                let res = null
                if (this.hasSubMenu) {
                    if (this.isMenuItem) {
                        res = 'caret-right'
                    } else {
                        res = 'caret-down'
                    }
                } else if (this.isMenuItem) {
                    res = 'empty'
                }
                return res
            },
            hasSubMenu() {
                return !!this.$slots.default;
            },
        }
    }
</script>
