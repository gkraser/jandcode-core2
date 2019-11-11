<template>
    <q-layout view="hHh Lpr fff" class="jc-app">

        <q-header elevated class="jc-app--header">
            <q-toolbar>

                <q-btn dense flat
                       icon="menu" @click="left = !left"/>

                <slot name="title">
                    <q-btn flat no-caps no-wrap :ripple="false" @click="callApp('home')">
                        <q-icon :name="icon" size="32px"/>
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
                  side="left" bordered content-class="jc-app--left"
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
    import {BaseAppMixin} from './mixins'

    export default {
        components: {},
        mixins: [BaseAppMixin],
        props: {},
        data() {
            return {
                left: false,
                leftWidth: 280,
            }
        },
        computed: {},
        methods: {}
    }
</script>
