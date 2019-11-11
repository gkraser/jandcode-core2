<template>
    <BaseApp :title="$jc.cfg.tst.fileNameBase" :class="appClasses">

        <template #title>
        </template>

        <template #toolbar>
        </template>

        <template #left>
            <q-item-label header>Главное меню</q-item-label>

            <TestMenu1 :items="items1" :levels="3"/>
        </template>

        <template #main>
            <q-page class="q-pa-lg">
                <q-toggle label="Theme1" left-label v-model="appClasses.theme1"/>
            </q-page>
        </template>

    </BaseApp>
</template>

<script>
    import {apex} from './vendor'
    import TestMenu1 from './comp/TestMenu1'

    export default {
        components: {
            BaseApp: apex.baseapp.JcBaseAppStd,
            TestMenu1
        },

        mixins: [apex.baseapp.AppMixin],

        props: {},
        data() {
            return {
                appClasses: {
                    'theme1': false
                },
                items1: [
                    {text: 'Элемент c font', icon: 'font1'},
                    {text: 'Элемент без иконки', icon: '', defaultOpened: false},
                    {text: 'Элемент с svg', icon: 'svg1'},
                    {text: 'Элемент с png', icon: 'png1'},
                ],
            }
        },
        created() {
        },
        computed: {},
        methods: {
            home() {
            }
        }
    }
</script>

<style lang="scss">


    @import "../../css/apex/vars.scss";

    .theme1 {
        .jc-app--header.q-header {
            background-color: $lime-1;
            color: $grey-7;
        }

        .jc-app--left {

            .jc-side-menu {

                font-size: 13px;

                .q-list--dense > .q-item, .q-item--dense {
                    min-height: 28px;
                }

                .q-item__section--avatar {
                    min-width: 24px;
                    padding-right: 12px;

                    .q-icon {
                        font-size: 20px;
                    }

                }

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
    }

</style>

