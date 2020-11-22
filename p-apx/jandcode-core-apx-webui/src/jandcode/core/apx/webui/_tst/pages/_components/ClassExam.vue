<template>
    <div class="row items-center q-mb-sm q-col-gutter-sm">
        <div :class="cls" class="col-10">
            <slot></slot>
        </div>
        <div class="col-2">
            <q-badge align="middle" outline1 color="primary" :label="cls"/>
        </div>
    </div>
</template>

<script>
/* Демонстрация класса для типографики
----------------------------------------------------------------------------- */

export default {
    props: {
        cls: {
            default: 'text'
        }
    },
    data() {
        return {}
    },
    methods: {},
}
</script>
