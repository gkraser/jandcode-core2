<template>
    <q-list :class="classes" :dense="true" :bordered="bordered">
        <slot></slot>
    </q-list>
</template>

<script>
    import {jsaBase} from '../vendor';

    let nm = 'jc-side-menu'

    export default {
        name: nm,
        props: {
            bordered: {
                type: Boolean,
                default: false
            },

            accordion: {
                type: Boolean,
                default: true
            }
        },
        data() {
            return {
                group: jsaBase.nextId(nm)
            }
        },
        computed: {

            classes() {
                return [
                    nm
                ]
            },

        },
    }
</script>
