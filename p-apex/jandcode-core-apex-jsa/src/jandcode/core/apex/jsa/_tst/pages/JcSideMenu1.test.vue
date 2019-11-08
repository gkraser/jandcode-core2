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
                    {text: 'Элемент c font', icon: 'font1'},
                    {text: 'Элемент без иконки', icon: '', defaultOpened: false},
                    {text: 'Элемент с svg', icon: 'svg1'},
                    {text: 'Элемент с png', icon: 'png1'},
                ],

                itemsNoIcon: [
                    {text: 'Элемент 1'},
                    {text: 'Элемент 2'},
                    {text: 'Элемент 3'},
                    {text: 'Элемент 4'},
                ],

                itemsFs: [
                    {text: 'Папка 1', icon: 'folder'},
                    {text: 'Папка 2', icon: 'folder'},
                    {text: 'Файл 3', icon: 'file'},
                    {text: 'Файл 4', icon: 'file'},
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

            &.q-list .jc-side-menu-item--level-0 {
                color: red;
                padding-top: 8px;
                padding-bottom: 8px;
            }

            .jc-side-menu-item--list-level-1 {
                background-color: gray;
            }

            .jc-side-menu-item--list-level-2 {
                background-color: silver;
            }

            &.q-list .jc-side-menu-item--level-2 {
                color: green;
                padding-top: 18px;
                padding-bottom: 18px;
            }

        }

    }

</style>

