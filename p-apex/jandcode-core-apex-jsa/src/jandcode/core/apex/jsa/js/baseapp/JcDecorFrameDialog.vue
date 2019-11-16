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

            <q-btn dense flat icon="close" v-close-popup>
                <q-tooltip>Close</q-tooltip>
            </q-btn>
        </q-bar>

        <q-card-section class="jc-frame--body">
            <slot name="default">
            </slot>
        </q-card-section>

        <q-bar class="jc-frame--footer">
            footer
        </q-bar>

    </q-card>
</template>

<script>
    import JcDecorFrame from "./JcDecorFrame"

    export default {
        extends: JcDecorFrame,
        props: {
            size: {
                default: null
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
            }
        }
    }
</script>
