<template>
    <jc-toolbar :key="toolbarSet" :class="toolbarClass">
        <template v-if="toolbarSet=='set1'">
            <div style="display:inline-block">М</div>
            <jc-action icon="bus"/>
            <jc-action icon="star"/>
            <jc-action icon="mail"/>
            <div style="display:inline-block">М</div>
            <jc-action icon="calendar"/>
            <jc-action icon="inbox"/>
            <div style="display:inline-block">М</div>
        </template>

        <template v-if="toolbarSet=='menu1'">
            <jc-action label="Конфигурации" icon="config"/>
            <jc-action icon="calc"/>
            <jc-action label="Задачи"/>
            <div style="display:inline-block">Метка</div>
            <jc-action label="Управление" icon="admin"/>

            <jc-action icon="calc" label="Меню 0">
                <SubMenu1/>
            </jc-action>
            <jc-action icon="bus">
                <div class="q-pa-md q-gutter-sm row" style="width:700px">
                    <q-btn label="Кнопка1 закроет" color="positive" v-close-popup/>
                    <q-btn label="Просто кнопка2" color="positive"/>
                    <q-btn label="Просто кнопка3" color="positive"/>
                </div>
            </jc-action>
            <jc-action label="Меню 1">
                <SubMenu1/>
            </jc-action>

            <jc-action icon="mail"/>
        </template>

        <template v-if="toolbarSet=='menu2'">
            <jc-action label="Конфигурации" icon="app-logo"/>
            <jc-action icon="app-logo"/>
            <jc-action label="Задачи"/>
            <div style="display:inline-block">Метка</div>
            <jc-action label="Управление" icon="admin"/>
        </template>

        <template v-if="toolbarSet=='logo1'">
            <jc-action icon="menu"/>
            <jc-toolbar-logo icon="app-logo" @click="own.home()"/>
            <jc-toolbar-title text="Заголовок приложения" @click="own.home()"/>
            <jc-action icon="star"/>
            <jc-action icon="mail"/>
            <jc-toolbar-logo icon="app-logo"/>
            <jc-toolbar-title text="ThemeNav"/>
            <img :src="logoUrl" style="width:90px"/>
            <jc-toolbar-logo icon="app-logo"/>
            <jc-action icon="star"/>
            <jc-toolbar-logo icon="app-logo"/>
            <jc-toolbar-title text="Заголовок" text2="Это подзаголовок"/>
        </template>

        <template v-if="toolbarSet=='button1'">
            <q-separator vertical/>
            <jc-action icon="mail"/>
            <q-btn class="jc-action" flat icon="mail">
                <q-badge floating color="orange">2</q-badge>
            </q-btn>

            <q-separator vertical/>
            <jc-action label="Action"/>
            <q-btn class="jc-action" flat no-caps label="Button"/>

            <q-separator vertical/>
            <jc-action icon="mail" label="Action"/>
            <q-btn class="jc-action" flat no-caps icon="mail" label="Button"/>

            <q-separator vertical/>
            <jc-action icon="mail" label="Action">
                <jc-action label="Action"/>
            </jc-action>
            <q-btn class="jc-action" flat no-caps icon="mail" label="Button">
                <q-menu>
                    <jc-action label="Action"/>
                </q-menu>
            </q-btn>
        </template>

        <template v-if="toolbarSet=='button2'">
            <q-separator vertical/>
            <jc-action icon="mail"/>
            <q-btn icon="mail">
                <q-badge floating color="orange">2</q-badge>
            </q-btn>

            <q-separator vertical/>
            <jc-action label="Action"/>
            <q-btn label="Button"/>

            <q-separator vertical/>
            <jc-action icon="mail" label="Action"/>
            <q-btn icon="mail" label="Button"/>

            <q-separator vertical/>
            <jc-action icon="mail" label="Action">
                <jc-action label="Action"/>
            </jc-action>
            <q-btn icon="mail" label="Button">
                <q-menu>
                    <jc-action label="Action"/>
                </q-menu>
            </q-btn>
        </template>
    </jc-toolbar>
</template>

<script>
import {jsaBase} from '../../vendor'

export let SubMenu1 = {
    functional: true,

    render(h, ctx) {
        function sub1() {
            return [
                h('jc-action', {attrs: {label: 'Без иконки пункт'}}),
                h('jc-action', {attrs: {label: 'Этот с иконкой ', icon: 'bus'}}),
                h('jc-action', {
                    attrs: {
                        label: 'Этот тоже с иконкой ', icon: 'mail'
                    }
                }),
                h('jc-action', {attrs: {label: 'Опять без иконки пункт'}}),
            ]
        }

        function sub2() {
            return [
                h('jc-action', {attrs: {label: 'Без иконки пункт'}}),
                h('jc-action', {
                    attrs: {
                        label: 'Этот с иконкой ', icon: 'bus'
                    }
                }, [sub1()]),
                h('jc-action', {
                    attrs: {
                        label: 'Этот тоже с иконкой ', icon: 'mail'
                    }
                }),
                h('jc-action', {attrs: {label: 'Опять без иконки пункт'}}),
            ]
        }

        function sub3() {
            return [
                h('jc-action', {attrs: {label: 'Без иконки пункт'}}),
                h('jc-action', {
                    attrs: {
                        label: 'Этот тоже без иконки'
                    }
                }, [sub1()]),
                h('jc-action', {
                    attrs: {
                        label: 'Этот тоже '
                    }
                }),
                h('jc-action', {attrs: {label: 'Опять без иконки пункт'}}),
            ]
        }

        return [
            h('jc-action', {attrs: {label: 'Без иконки пункт'}}, [sub2()]),
            h('jc-action', {attrs: {label: 'Этот с иконкой ', icon: 'bus'}}),
            h('jc-action', {
                attrs: {
                    label: 'Этот тоже с иконкой и имя его длиннное', icon: 'mail'
                }
            }, [sub1()]),
            h('jc-action', {attrs: {label: 'Опять без иконки пункт'}}),
            h('jc-action', {attrs: {label: 'Без иконки подменю'}}, [sub3()]),
        ]
    }
}

export function createToolbarSets() {
    return [
        {value: 'set1', label: 'Просто иконки'},
        {value: 'menu1', label: 'Меню 1'},
        {value: 'menu2', label: 'Меню 2'},
        {value: 'logo1', label: 'Логотип 1'},
        {value: 'button1', label: 'Кнопки 1'},
        {value: 'button2', label: 'Кнопки 2'},
    ]
}

export default {
    components: {
        SubMenu1
    },
    props: {
        toolbarSet: {
            default: ''
        },
        toolbarClass: ''
    },
    data() {
        return {
            logoUrl: jsaBase.url.ref('jandcode/core/apx/webapp/_tst/images/logo-my-1.svg')
        }
    },
    methods: {
        clickItem() {
            //todo apx.showMsg("Событие click")
        }
    },
}
</script>

<style lang="less">
</style>
