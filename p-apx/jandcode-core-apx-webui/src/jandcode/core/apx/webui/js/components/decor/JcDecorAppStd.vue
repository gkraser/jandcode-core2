<template>
    <q-layout view="hHh Lpr fff" class="jc-app jc-decor-app-std" :container="container">

        <q-header :elevated="false" class="jc-decor-app-std__header">
            <slot name="top-header"></slot>
            <q-toolbar>
                <slot name="toolbar-left">
                    <jc-toolbar>
                        <jc-action
                                icon="menu" @click="own.left = !own.left"/>
                        <slot name="title">
                            <q-icon v-if="own.icon" :name="own.icon"
                                    class="cursor-pointer jc-decor-app-std__logo"
                                    @click="own.home()"/>
                            <jc-toolbar-title :text="own.title" :text2="own.title2"
                                              @click="own.home()"/>
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
                  side="left" bordered
                  content-class="jc-decor-app-std__side jc-decor-app-std__side--left"
                  :width="own.leftWidth">
            <q-scroll-area class="fit">

                <slot name="left">
                </slot>

            </q-scroll-area>
        </q-drawer>

        <q-drawer v-model="own.right" behavior="mobile" :elevated="false"
                  no-swipe-open no-swipe-close no-swipe-backdrop
                  side="right" bordered
                  content-class="jc-decor-app-std__side jc-decor-app-std__side--right"
                  :width="own.rightWidth">
            <q-scroll-area class="fit">

                <slot name="right">
                </slot>

            </q-scroll-area>
        </q-drawer>

        <q-page-container class="jc-app--main jc-decor-app-std__main">
            <slot name="main">
                <q-page>
                    <jc-shower-main></jc-shower-main>
                </q-page>
            </slot>
        </q-page-container>

    </q-layout>
</template>

<script>
import JcDecorApp from "../../baseapp/JcDecorApp"

export default {
    name: 'jc-decor-app-std',

    extends: JcDecorApp,

    props: {
        container: {
            type: Boolean,
            default: null
        }
    }
}
</script>
