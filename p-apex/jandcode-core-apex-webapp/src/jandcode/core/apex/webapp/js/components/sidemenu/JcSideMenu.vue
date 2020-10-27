<template>
    <q-list :class="classes" :bordered="bordered">
        <slot></slot>
    </q-list>
</template>

<script>
import {jsaBase} from '../vendor';

let nm = 'jc-side-menu'

/*

doc:

event:opened-change
    Изменилось opened у какого-то  дочернего

 */

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

    provide() {
        return {
            parentMenu: this
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
