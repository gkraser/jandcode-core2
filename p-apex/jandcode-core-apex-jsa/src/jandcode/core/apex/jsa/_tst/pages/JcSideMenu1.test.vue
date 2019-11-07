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
                  side="left" bordered content-class="bg-grey-1"
                  :width="leftWidth">
            <q-scroll-area class="fit">

                <q-item-label header>Главное меню</q-item-label>

                <TestMenu1 :items="items1" :levels="3"/>


            </q-scroll-area>
        </q-drawer>

        <q-page-container>
            <q-page>
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

                items1: [
                    {text: 'Элемент 111с font', icon: 'font1'},
                    {text: 'Элемент без иконки', icon: '', defaultOpened: false},
                    {text: 'Элемент с svg', icon: 'svg1'},
                    {text: 'Элемент с png', icon: 'png1'},
                ],
                itemsNoIcon: [
                    {text: 'Элемент 111с font'},
                    {text: 'Элемент без иконки'},
                    {text: 'Элемент с svg'},
                    {text: 'Элемент с png'},
                ],
                itemsFs: [
                    {text: 'Элемент 111с font', icon: 'folder'},
                    {text: 'Элемент без иконки', icon: 'folder', defaultOpened: true},
                    {text: 'Элемент с svg', icon: 'file'},
                    {text: 'Элемент с png', icon: 'file'},
                ],

            }
        },
    }
</script>

<style lang="scss">
</style>

