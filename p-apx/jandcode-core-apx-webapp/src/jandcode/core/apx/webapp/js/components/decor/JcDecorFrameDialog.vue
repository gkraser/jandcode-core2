<template>
    <q-card :class="classes">

        <div class="jc-frame--header">
            <jc-toolbar>

                <jc-toolbar-logo v-if="own.icon" :icon="own.icon"
                                 :class="own.title2?'self-start':''">
                </jc-toolbar-logo>

                <jc-toolbar-title :text="own.title" :text2="own.title2">
                </jc-toolbar-title>

                <q-space/>

                <slot name="toolbar">
                </slot>

                <q-btn dense flat icon="close" @click="own.closeFrame('cancel')"/>

            </jc-toolbar>
        </div>

        <q-card-section class="jc-frame--body" :class="bodyClass" :style="bodyStyle">
            <slot name="default">
            </slot>
        </q-card-section>

        <div class="jc-frame--footer">
            <jc-toolbar>
                <q-space/>
                <template v-for="b in footerRightButtons">
                    <jc-btn :label="b.label" :kind="b.kind" :icon="b.icon"
                            :flat="flatButtons" @click="own.closeFrame(b.cmd)"/>
                </template>
            </jc-toolbar>
        </div>

    </q-card>
</template>

<script>
import JcDecorFrame from '../../baseapp/JcDecorFrame'
import {getDialogButtons} from '../../baseapp/frame'

export default {
    extends: JcDecorFrame,
    props: {
        size: {
            default: null
        },
        buttons: {
            default: 'ok-cancel',
        }
    },

    computed: {
        classes() {
            let res = [
                'jc-frame',
                'jc-decor-dialog',
            ]
            if (this.size) {
                let s = '' + this.size
                res.push('jc-decor-dialog--size__' + s)
            }
            return res;
        },
        footerRightButtons() {
            return getDialogButtons(this.buttons)
        },
        flatButtons() {
            return this.$jc.cfg.theme.dialog.flatButtons
        }
    }
}
</script>
