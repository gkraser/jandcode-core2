<template>
    <q-layout view="hHh Lpr fff" class="jc-app">

        <q-header elevated class="bg-lime-1 text-grey-7 jc-app--header">
            <q-toolbar>

                <q-btn dense flat
                       icon="menu" @click="left = !left"/>

                <slot name="title">
                    <q-btn flat no-caps no-wrap :ripple="false" @click="doCmd('home')">
                        <q-icon :name="icon" size="32px" color="orange"/>
                        <q-toolbar-title shrink>
                            {{title}}
                        </q-toolbar-title>
                    </q-btn>
                </slot>

                <q-space/>

                <slot name="toolbar">
                </slot>

            </q-toolbar>
        </q-header>

        <q-drawer v-model="left" show-if-above
                  side="left" bordered content-class="bg-grey-1 jc-app--left"
                  :width="leftWidth">
            <q-scroll-area class="fit">

                <slot name="left">
                </slot>

            </q-scroll-area>
        </q-drawer>

        <q-page-container class="jc-app--main">
            <slot name="main">
            </slot>
        </q-page-container>

    </q-layout>
</template>

<script>
    export default {
        components: {},
        props: {
            title: {
                default: 'Без заголовка'
            },
            icon: {
                default: 'app-logo'
            }
        },
        data() {
            return {
                left: false,
                leftWidth: 280,
            }
        },
        methods: {

            /**
             * Выполнить метод cmd с аргументами args
             * у родителя компонента, если у него есть такой метод.
             * Если у него нету а есть у меня - выполнить мой.
             *
             * @param cmd
             * @param args
             * @return {*}
             */
            doCmd(cmd, ...args) {
                let own = this.$parent;
                if (own) {
                    if (own[cmd]) {
                        return own[cmd](...args)
                    }
                }
                if (this[cmd]) {
                    return this[cmd](...args)
                }
            }
        }
    }
</script>

<style lang="scss">


    @import "../../css/apex/vars.scss";

    .jc-app--left {

        .jc-side-menu {

            color: $grey-9;

            .q-item__section--avatar {
                .q-icon {
                    opacity: 0.6;
                    color: $orange-6;
                    font-size: 18px;
                }
            }

            .jc-side-menu-item--level-0 {
                font-size: 14px;
                padding-top: 10px;
                padding-bottom: 10px;

                .q-item__section--avatar {
                    .q-icon {
                        font-size: 24px;
                    }
                }

            }

            .jc-side-menu-item--list-level-1 {
                background-color: $grey-2;
            }

        }
    }

</style>

