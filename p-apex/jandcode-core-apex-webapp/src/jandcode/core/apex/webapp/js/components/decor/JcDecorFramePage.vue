<template>
    <component :is="rootComponentName" class="jc-frame jc-decor-page">

        <div class="jc-frame--header">

            <jc-toolbar v-if="hasTitle">


                <jc-toolbar-logo v-if="own.icon" :icon="own.icon">
                </jc-toolbar-logo>

                <jc-toolbar-title :text="own.title" :text2="own.title2">
                </jc-toolbar-title>

                <q-space/>

                <slot name="toolbar">
                </slot>

            </jc-toolbar>

        </div>

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
