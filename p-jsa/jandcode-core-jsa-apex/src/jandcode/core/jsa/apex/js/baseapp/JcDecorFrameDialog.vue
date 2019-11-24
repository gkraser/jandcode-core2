<template>
    <q-card :class="classes">

        <q-bar class="jc-frame--header">

            <q-icon v-if="hasIcon" :name="own.icon"/>

            <div class="jc-frame--title">
                {{own.title}}
            </div>

            <q-space/>

            <slot name="toolbar">
            </slot>

            <q-btn dense flat icon="close" @click="own.closeFrame('cancel')"/>
        </q-bar>

        <q-card-section class="jc-frame--body" :class="bodyClass" :style="bodyStyle">
            <slot name="default">
            </slot>
        </q-card-section>

        <div class="jc-frame--footer row q-gutter-x-sm q-pa-sm">
            <q-space/>
            <template v-for="b in footerRightButtons">
                <q-btn :label="b.label" :color="b.color" :icon="b.icon"
                       :flat="flatButtons" @click="own.closeFrame(b.cmd)"/>
            </template>
        </div>

    </q-card>
</template>

<script>
    import JcDecorFrame from './JcDecorFrame'
    import {getDialogButtons} from './frame'


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
