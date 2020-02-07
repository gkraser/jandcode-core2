<template>
    <q-item v-if="!hasItems" clickable :disable="disable"
            :style="style" :class="classes" @click="onClick"
            :to="to" :replace="replace" exact>
        <q-item-section avatar>
            <q-icon :name="iconValue"/>
        </q-item-section>
        <q-item-section>
            {{label}}
        </q-item-section>
    </q-item>
    <q-expansion-item v-else ref="expansionItem"
                      expand-separator1 :disable="disable"
                      :value="opened"
                      :group="groupValue"
                      :headerStyle="style"
                      :headerClass="classes"
                      @click="onClick"
                      @input="onInput">
        <template v-slot:header>
            <q-item-section avatar>
                <q-icon :name="iconValue"/>
            </q-item-section>
            <q-item-section>
                {{label}}
            </q-item-section>
        </template>
        <q-list :class="classesList">
            <slot></slot>
        </q-list>
    </q-expansion-item>
</template>

<script>
    import {jsaVue, jsaBase} from '../vendor';
    import JcSideMenu from './JcSideMenu'

    let nm = 'jc-side-menu-item'

    export let cfg = {
        insetPaddingStart: 16,
        insetPaddingLevel: 32,
    }

    export default {
        name: nm,
        props: {
            icon: {
                type: String,
                default: ''
            },

            label: {
                type: String
            },

            disable: {
                type: Boolean
            },

            opened: {
                type: Boolean,
                default: false
            },

            to: [Object, String],
            replace: Boolean,
        },

        inject: {
            parentMenu: {default: null}
        },

        data() {
            return {
                group: jsaBase.nextId(nm),
            }
        },
        computed: {

            classes() {
                return [
                    nm,
                    nm + '--level-' + this.level
                ]
            },

            classesList() {
                return [
                    nm + '--list-level-' + (this.level + 1)
                ]
            },

            style() {
                if (this.level > 0) {
                    let pad = (cfg.insetPaddingStart + this.level * cfg.insetPaddingLevel) + 'px'
                    return {
                        'padding-left': pad
                    }
                }
            },

            hasItems() {
                return !!this.$slots.default
            },

            groupValue() {
                if (this.parentMenu) {
                    if (!this.parentMenu.accordion) {
                        return
                    }
                }

                let parentItem = jsaVue.findCompUp(this.$parent, this.$options.name)
                if (parentItem) {
                    return parentItem.group
                }

                if (this.parentMenu) {
                    return this.parentMenu.group
                }

            },

            iconValue() {
                // если нет иконки, возвращаем пустую
                return this.icon || ' '
            },

            /**
             * Уровень, начиня с 0
             */
            level() {
                let own = jsaVue.findCompUp(this.$parent, this.$options.name)
                if (own) {
                    return own.level + 1
                } else {
                    return 0
                }
            },

        },
        methods: {
            onClick(ev) {
                this.$emit('click', ev, this)
            },
            onInput(val) {
                let oldVal = this.$refs.expansionItem.showing
                if (this.$listeners.input) {
                    this.$emit('input', val)
                } else {
                    this.$refs.expansionItem.showing = val
                }
                if (this.parentMenu && oldVal !== val) {
                    this.parentMenu.$emit('opened-change', this)
                }
            },
        }
    }
</script>
