<template>
    <component :is="rootComponentName" class="jc-frame jc-decor-page">

        <q-toolbar v-if="hasTitle" class="jc-frame--header">

            <q-toolbar-title shrink>
                <q-item>
                    <q-item-section v-if="hasIcon" class="jc-frame--icon"
                                    avatar>
                        <q-icon :name="own.icon"/>
                    </q-item-section>
                    <q-item-section>
                        <q-item-label class="jc-frame--title">
                            {{own.title}}
                        </q-item-label>
                        <q-item-label v-if="own.title2" class="jc-frame--title2"
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

        <div class="jc-frame--body" :class="bodyClass" :style="bodyStyle">
            <slot name="default">
            </slot>
        </div>
    </component>
</template>

<script>
    import JcDecorFrame from "../../baseapp/JcDecorFrame"

    export default {
        extends: JcDecorFrame,

        inject: {
            pageContainer: {
                default: false
            },
        },

        computed: {
            rootComponentName() {
                return this.pageContainer ? 'q-page' : 'div'
            }
        }

    }
</script>
