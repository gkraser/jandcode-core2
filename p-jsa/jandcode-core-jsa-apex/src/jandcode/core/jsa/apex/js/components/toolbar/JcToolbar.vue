<template>
    <div :class="classes">
        <slot name="default"/>
    </div>
</template>

<script>
    export default {
        name: 'jc-toolbar',
        props: {},
        data() {
            return {}
        },
        provide() {
            return {
                inToolbar: true
            }
        },
        methods: {},
        computed: {
            classes() {
                return ['row', 'items-center', 'jc-toolbar']
            }
        }
    }
</script>
