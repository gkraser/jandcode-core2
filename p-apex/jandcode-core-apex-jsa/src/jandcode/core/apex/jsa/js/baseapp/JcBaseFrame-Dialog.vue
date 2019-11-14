<template>
    <div class="jc-frame-page">
        <q-toolbar-title class="jc-frame-page--header">
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
                <q-item-section side>
                    <q-toolbar>
                        <slot name="toolbar">
                        </slot>
                    </q-toolbar>
                </q-item-section>
            </q-item>
        </q-toolbar-title>

        <div class="jc-frame-page--body">
            <slot name="default">
            </slot>
        </div>
    </div>
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
