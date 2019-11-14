<template>
    <div class="jc-frame-page">

        <q-toolbar class="jc-frame-page--header">

            <q-toolbar-title shrink>
                <q-item>
                    <q-item-section v-if="frame.icon" class="jc-frame-page--icon"
                                    avatar>
                        <q-icon :name="frame.icon||' '"/>
                    </q-item-section>
                    <q-item-section>
                        <q-item-label class="jc-frame-page--title">
                            {{frame.title}}
                        </q-item-label>
                        <q-item-label v-if="frame.title2" class="jc-frame-page--title2"
                                      caption>
                            {{frame.title2}}
                        </q-item-label>
                    </q-item-section>
                </q-item>
            </q-toolbar-title>

            <q-space/>

            <slot name="toolbar">
            </slot>

        </q-toolbar>

        <div class="jc-frame-page--body">
            <slot name="default">
            </slot>
        </div>
    </div>
</template>

<script>
    import {JcBaseDecorFrame} from "./base-decor"

    export default {
        extends: JcBaseDecorFrame,
    }
</script>
