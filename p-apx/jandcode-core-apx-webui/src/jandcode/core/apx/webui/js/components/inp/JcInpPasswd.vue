<template>
    <q-input
            v-on="$listeners"
            v-bind="$attrs"
            outlined
            :class="classes"
            type="password"
    >
    </q-input>
</template>

<script>
export default {
    name: 'jc-inp-passwd',
    props: {},
    computed: {
        classes() {
            let res = ['jc-inp']
            return res
        },
    },
}
</script>
