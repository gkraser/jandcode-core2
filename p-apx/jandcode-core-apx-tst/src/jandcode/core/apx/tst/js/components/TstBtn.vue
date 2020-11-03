<template>
    <button class="tst-btn" v-on="$listeners">{{ label }}</button>
</template>

<script>
export default {
    name: 'tst-btn',
    props: {
        label: {
            type: String,
        }
    },
}
</script>
