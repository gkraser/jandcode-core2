<template>
    <q-layout view="hHh Lpr fff" class="jc-app">

        <q-header elevated class="jc-app--header">
            <q-toolbar>

                <q-btn dense flat
                       icon="menu" @click="own.left = !own.left"/>

                <slot name="title">
                    <q-btn flat no-caps no-wrap :ripple="false" @click="own.home()">
                        <q-icon v-if="hasIcon" :name="own.icon" size="32px"/>
                        <q-toolbar-title shrink>
                            {{own.title}}
                        </q-toolbar-title>
                    </q-btn>
                </slot>

                <q-space/>

                <slot name="toolbar">
                </slot>

            </q-toolbar>
        </q-header>

        <q-drawer v-model="own.left" show-if-above
                  side="left" bordered content-class="jc-app--side jc-app--left"
                  :width="own.leftWidth">
            <q-scroll-area class="fit">

                <slot name="left">
                </slot>

            </q-scroll-area>
        </q-drawer>

        <q-drawer v-model="own.right" behavior="mobile"
                  no-swipe-open no-swipe-close no-swipe-backdrop
                  side="right" bordered content-class="jc-app--side jc-app--right"
                  :width="own.rightWidth">
            <q-scroll-area class="fit">

                <slot name="right">
                </slot>

            </q-scroll-area>
        </q-drawer>

        <q-page-container class="jc-app--main">
            <slot name="default">
            </slot>
        </q-page-container>

    </q-layout>
</template>

<script>
    import JcDecorApp from "./JcDecorApp"

    export default {
        extends: JcDecorApp,
    }
</script>
