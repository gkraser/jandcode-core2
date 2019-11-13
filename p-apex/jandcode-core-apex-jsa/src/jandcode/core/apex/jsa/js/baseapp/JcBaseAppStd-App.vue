<template>
    <q-layout view="hHh Lpr fff" class="jc-app">

        <q-header elevated class="jc-app--header">
            <q-toolbar>

                <q-btn dense flat
                       icon="menu" @click="app.left = !app.left"/>

                <slot name="title">
                    <q-btn flat no-caps no-wrap :ripple="false" @click="app.home()">
                        <q-icon :name="app.icon" size="32px"/>
                        <q-toolbar-title shrink>
                            {{app.title}}
                        </q-toolbar-title>
                    </q-btn>
                </slot>

                <q-space/>

                <slot name="toolbar">
                </slot>

            </q-toolbar>
        </q-header>

        <q-drawer v-model="app.left" show-if-above
                  side="left" bordered content-class="jc-app--side jc-app--left"
                  :width="app.leftWidth">
            <q-scroll-area class="fit">

                <slot name="left">
                </slot>

            </q-scroll-area>
        </q-drawer>

        <q-drawer v-model="app.right" behavior="mobile"
                  no-swipe-open no-swipe-close no-swipe-backdrop
                  side="right" bordered content-class="jc-app--side jc-app--right"
                  :width="app.rightWidth">
            <q-scroll-area class="fit">

                <slot name="right">
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
        computed: {
            app() {
                return this.$parent
            }
        }
    }
</script>
