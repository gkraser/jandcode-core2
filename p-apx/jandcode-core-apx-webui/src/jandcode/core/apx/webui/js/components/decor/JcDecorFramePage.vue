<template>
    <div :class="classes">

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

                <template v-if="own.isClosable">
                    <q-btn dense flat icon="close" @click="own.closeFrame('cancel')"/>
                </template>

            </jc-toolbar>

        </div>

        <div class="jc-decor-frame-page__body"
             :class="bodyClass"
             :style="bodyStyle">
            <slot name="default">
            </slot>
        </div>
    </div>
</template>

<script>
import JcDecorFrame from "../../baseapp/JcDecorFrame"

let nm = 'jc-decor-frame-page';

export default {
    name: nm,
    extends: JcDecorFrame,
    computed: {
        classes() {
            let res = [nm]
            if (this.bodyFit) {
                res.push(nm + '--body-fit')
            }
            return res
        }
    }
}
</script>
