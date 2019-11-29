<template>
    <q-btn v-else :class="classes" flat no-caps no-wrap
           align="left" :to="to" :replace="replace" :type="typeTag"
           v-on="$listeners" v-close-popup="isMenuItem && !hasSubMenu"
           :label="label" :icon="leftIcon" :icon-right="rightIcon">
        <template v-if="hasSubMenu">
            <q-menu content-class="jc-action--menu"
                    :anchor="isMenuItem?'top right':null"
                    square>
                <div class="column">
                    <slot name="default"></slot>
                </div>
            </q-menu>
        </template>
    </q-btn>
</template>

<script>
    export default {
        name: 'jc-action',
        props: {
            label: {},
            icon: {default: null},
            to: {},
            replace: {},
        },
        provide() {
            return {
                isMenuItem: true
            }
        },
        inject: {
            isMenuItem: {default: false}
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
            typeTag() {
                if (this.$listeners.click) {
                    return null
                } else {
                    return 'a'
                }
            },
        }
    }
</script>
