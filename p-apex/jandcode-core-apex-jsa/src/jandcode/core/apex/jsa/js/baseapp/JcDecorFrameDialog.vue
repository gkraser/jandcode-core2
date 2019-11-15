<template>
    <q-card class="jc-frame-dialog">

        <q-bar class="jc-frame-page--header">

            <q-icon v-if="hasIcon" :name="own.icon"/>

            <div class="jc-frame-page--title">
                {{own.title}}
            </div>

            <q-space/>

            <slot name="toolbar">
            </slot>

            <q-btn dense flat icon="close" v-close-popup>
                <q-tooltip>Close</q-tooltip>
            </q-btn>
        </q-bar>

        <q-card-section class="jc-frame-page--body">
            <slot name="default">
            </slot>
        </q-card-section>
    </q-card>
</template>

<script>
    import JcDecor from "./JcDecor"

    export default {
        extends: JcDecor,
    }
</script>
