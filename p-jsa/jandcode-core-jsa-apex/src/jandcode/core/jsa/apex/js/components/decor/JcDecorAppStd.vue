<template>
    <q-layout view="hHh Lpr fff" class="jc-app jc-decor-app">

        <q-header :elevated="false" class="jc-app--header">
            <q-toolbar>
                <slot name="toolbar-left">
                    <jc-toolbar>
                        <jc-action
                                icon="menu" @click="own.left = !own.left"/>
                        <slot name="title">
                            <jc-toolbar-logo :icon="own.icon" @click="own.home()"/>
                            <jc-toolbar-title :text="own.title" @click="own.home()"/>
                        </slot>
                    </jc-toolbar>
                </slot>

                <q-space/>

                <slot name="toolbar-right">
                    <jc-toolbar>
                        <slot name="toolbar">
                        </slot>
                    </jc-toolbar>
                </slot>

            </q-toolbar>
        </q-header>

        <q-drawer v-model="own.left" :elevated="false"
                  side="left" bordered content-class="jc-app--side jc-app--left"
                  :width="own.leftWidth">
            <q-scroll-area class="fit">

                <slot name="left">
                </slot>

            </q-scroll-area>
        </q-drawer>

        <q-drawer v-model="own.right" behavior="mobile" :elevated="false"
                  no-swipe-open no-swipe-close no-swipe-backdrop
                  side="right" bordered content-class="jc-app--side jc-app--right"
                  :width="own.rightWidth">
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
    import JcDecorApp from "../../baseapp/JcDecorApp"

    export default {
        extends: JcDecorApp,
    }
</script>
