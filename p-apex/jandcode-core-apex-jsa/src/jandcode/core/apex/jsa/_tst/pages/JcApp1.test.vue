<template>
    <App>

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
                <q-toggle label="right" left-label v-model="right"/>
            </q-page>
        </template>

    </App>
</template>

<script>
    import {apex} from './vendor'
    import TestMenu1 from './comp/TestMenu1'

    export default {
        extends: apex.JcApp,

        components: {
            TestMenu1
        },

        created() {
            this.title = this.$jc.cfg.tst.fileName
        },

        data() {
            return {
                items1: [
                    {text: 'Элемент c font', icon: 'font1'},
                    {text: 'Элемент без иконки', icon: '', defaultOpened: false},
                    {text: 'Элемент с svg', icon: 'svg1'},
                    {text: 'Элемент с png', icon: 'png1'},
                ],
            }
        },
        computed: {},
        methods: {}
    }
</script>

<style lang="scss">
</style>

