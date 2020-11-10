<template>
    <div class="jc-decor-frame-page">

        <div class="jc-decor-frame-page__header">

            <jc-toolbar v-if="hasTitle">


                <q-icon v-if="own.icon" :name="own.icon"
                        :class="[own.title2?'self-start':'', 'jc-decor-frame-page__title-icon']">
                </q-icon>

                <jc-toolbar-title :text="own.title" :text2="own.title2">
                </jc-toolbar-title>

                <q-space/>

                <slot name="toolbar">
                </slot>

            </jc-toolbar>

        </div>

        <div class="jc-decor-frame-page__body" :class="bodyClass" :style="bodyStyle">
            <slot name="default">
            </slot>
        </div>
    </div>
</template>

<script>
import JcDecorFrame from "../../baseapp/JcDecorFrame"

export default {
    name: 'jc-decor-frame-page',
    extends: JcDecorFrame,
}
</script>
