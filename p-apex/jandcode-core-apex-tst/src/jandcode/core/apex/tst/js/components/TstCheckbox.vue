<template>
    <label class="tst-checkbox">
        <input type="checkbox" :checked="value" @change="onChange">
        <span>{{ label }}</span>
    </label>
</template>

<script>
export default {
    name: 'tst-checkbox',
    props: {
        label: {
            type: String,
        },
        value: null
    },
    methods: {
        onChange: function(ev) {
            this.$emit('input', ev.target.checked);
        }
    }
}
</script>
