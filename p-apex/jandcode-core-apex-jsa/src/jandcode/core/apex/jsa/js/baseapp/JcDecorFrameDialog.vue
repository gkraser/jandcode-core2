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

        <q-separator/>

        <div class="jc-frame--footer row q-gutter-x-sm q-pa-sm">
            <q-space/>
            <q-btn label="Ок"/>
            <q-btn label="Отмена"/>
        </div>

    </q-card>
</template>

<script>
    import JcDecorFrame from './JcDecorFrame'
    import {dialogButtons} from './frame'


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
