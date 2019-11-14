<template>
    <q-card class="jc-frame-dialog">

        <q-bar class="jc-frame-page--header">

            <q-icon :name="frame.icon||' '"/>

            <div class="jc-frame-page--title">
                {{frame.title}}
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
    export default {
        computed: {
            frame() {
                return this.$parent
            }
        }
    }
</script>
