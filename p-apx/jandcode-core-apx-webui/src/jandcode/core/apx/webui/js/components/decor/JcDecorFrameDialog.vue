<template>
    <div :class="classes">

        <div class="jc-decor-frame-dialog__header">
            <jc-toolbar>

                <q-icon v-if="own.icon" :name="own.icon"
                        :class="['jc-decor-frame-dialog__title-icon']">
                </q-icon>

                <jc-toolbar-title :text="own.title" :text2="own.title2">
                </jc-toolbar-title>

                <q-space/>

                <slot name="toolbar">
                </slot>

                <q-btn dense flat icon="close" @click="own.closeFrame('cancel')"/>

            </jc-toolbar>
        </div>

        <div :class="classesBody" :style="bodyStyle">
            <slot name="default">
            </slot>
        </div>

        <div class="jc-decor-frame-dialog__footer">
            <jc-toolbar>
                <q-space/>
                <template v-for="b in footerRightButtons">
                    <jc-btn :label="b.label" :kind="b.kind" :icon="b.icon"
                            @click="own.closeFrame(b.cmd)"/>
                </template>
            </jc-toolbar>
        </div>

    </div>
</template>

<script>
import JcDecorFrame from '../../baseapp/JcDecorFrame'
import {getDialogButtons} from '../../baseapp/frame'

export default {
    name: 'jc-decor-frame-dialog',

    extends: JcDecorFrame,
    props: {
        size: {
            default: null
        },
        buttons: {
            default: 'ok-cancel',
        },
        bodyFit: {
            type: Boolean
        },
        bodyClass: {},
        bodyStyle: {},
        noPadding: {
            type: Boolean
        }
    },

    computed: {
        classes() {
            let res = [
                'jc-decor-frame-dialog',
            ]
            if (this.size) {
                let s = '' + this.size
                res.push('jc-decor-frame-dialog--size-' + s)
            }
            return res;
        },
        classesBody() {
            let res = [
                'jc-decor-frame-dialog__body',
            ]
            if (this.bodyClass) {
                res.push(this.bodyClass)
            }
            if (this.bodyFit) {
                res.push('jc-decor-frame-dialog__body--fit')
            }
            if (!this.noPadding) {
                res.push('jc-decor-frame-dialog__body--padding')
            }
            return res;
        },
        footerRightButtons() {
            return getDialogButtons(this.buttons)
        },
    }
}
</script>
