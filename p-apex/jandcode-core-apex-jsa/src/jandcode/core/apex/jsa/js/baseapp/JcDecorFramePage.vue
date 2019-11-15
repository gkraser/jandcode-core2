<template>
    <div class="jc-frame-page">

        <q-toolbar class="jc-frame-page--header">

            <q-toolbar-title shrink>
                <q-item>
                    <q-item-section v-if="hasIcon" class="jc-frame-page--icon"
                                    avatar>
                        <q-icon :name="own.icon"/>
                    </q-item-section>
                    <q-item-section>
                        <q-item-label class="jc-frame-page--title">
                            {{own.title}}
                        </q-item-label>
                        <q-item-label v-if="own.title2" class="jc-frame-page--title2"
                                      caption>
                            {{own.title2}}
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
    import JcDecor from "./JcDecor"

    export default {
        extends: JcDecor,
    }
</script>
