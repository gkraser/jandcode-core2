<template>
    <jc-btn v-on="$listeners"
            v-bind="$attrs"
            v-close-popup="isMenuItem && !hasSubMenu"
            :class="classes"
            :flat="inToolbar || isMenuItem"
            align="left"
            :stretch="isMenuItem?true:stretch"
            :icon="leftIcon"
            :icon-right="rightIcon">
        <slot name="content"></slot>
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
/*

doc:

slot:content
    Содержимое внутри action. Используется, например, для badge

 */
export default {
    name: 'jc-action',
    props: {
        icon: {default: null},
        stretch: {default: null},
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
            } else if (!res) {
                res = undefined
            }
            return res
        },
        rightIcon() {
            let res = undefined
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
