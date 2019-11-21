<template>
    <q-layout view="hHh Lpr fff">

        <q-header elevated class="bg-lime-1 text-grey-7">
            <q-toolbar>

                <q-btn dense flat
                       icon="menu" @click="left = !left"/>

                <q-btn flat no-caps no-wrap :ripple="false">
                    <q-icon name="app-logo" size="32px"/>
                    <q-toolbar-title shrink>
                        {{$jc.cfg.tst.fileName}}
                    </q-toolbar-title>
                </q-btn>

                <q-space/>

            </q-toolbar>
        </q-header>

        <q-drawer v-model="left" show-if-above
                  side="left" bordered :content-class="sideClasses"
                  :width="leftWidth">
            <q-scroll-area class="fit">

                <q-item-label header>Главное меню</q-item-label>

                <TestMenu1 :items="items" :levels="3" @click="onClick"/>


            </q-scroll-area>
        </q-drawer>

        <q-page-container>
            <q-page class="q-pa-lg">

                <q-toggle label="Theme1" left-label v-model="sideClasses.theme1"/>

                <q-btn color="teal" label="items1" @click="curItemsName='items1'"/>
                <q-btn color="teal" label="itemsNoIcon"
                       @click="curItemsName='itemsNoIcon'"/>
                <q-btn color="teal" label="itemsFs" @click="curItemsName='itemsFs'"/>

                <div v-for="n in 20" style="padding:20px">{{n}}</div>
            </q-page>
        </q-page-container>

    </q-layout>
</template>

<script>
    import TestMenu1 from './comp/TestMenu1'

    export default {
        components: {
            TestMenu1

        },
        props: {},
        data() {
            return {
                left: false,
                leftWidth: 300,

                sideClasses: {
                    'bg-grey-1': true,
                    'theme1': false
                },

                curItemsName: 'items1',

                items1: [
                    {label: 'Элемент c font', icon: 'font1'},
                    {label: 'Элемент без иконки', icon: '', defaultOpened: false},
                    {label: 'Элемент с svg', icon: 'svg1'},
                    {label: 'Элемент с png', icon: 'png1'},
                ],

                itemsNoIcon: [
                    {label: 'Элемент 1'},
                    {label: 'Элемент 2'},
                    {label: 'Элемент 3'},
                    {label: 'Элемент 4'},
                ],

                itemsFs: [
                    {label: 'Папка 1', icon: 'folder'},
                    {label: 'Папка 2', icon: 'folder'},
                    {label: 'Файл 3', icon: 'file'},
                    {label: 'Файл 4', icon: 'file'},
                ],

            }
        },
        computed: {
            items() {
                return this[this.curItemsName]
            }
        },
        methods: {
            onClick(ev, th) {
                console.info("click", arguments);
            }
        }
    }
</script>

<style lang="scss">

    .theme1 {

        .jc-side-menu {

            .jc-side-menu-item--level-0 {
                color: red;
                padding-top: 8px;
                padding-bottom: 8px;

                .q-item__section--avatar {
                    .q-icon {
                        font-size: 44px;
                    }
                }

            }

            .jc-side-menu-item--level-1 {
                .q-item__section--avatar {
                    .q-icon {
                        font-size: 10px;
                    }
                }
            }

            .jc-side-menu-item--list-level-1 {
                background-color: gray;
            }

            .jc-side-menu-item--list-level-2 {
                background-color: silver;
            }

            .jc-side-menu-item--level-2 {
                color: green;
                padding-top: 18px;
                padding-bottom: 18px;
            }

        }

    }

</style>

