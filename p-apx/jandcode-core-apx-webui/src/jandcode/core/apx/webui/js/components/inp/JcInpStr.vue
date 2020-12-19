<template>
    <q-input
            v-on="$listeners"
            v-bind="$attrs"
            outlined
            :class="classes"
    >
    </q-input>
</template>

<script>
export default {
    name: 'jc-inp-str',
    props: {},
    computed: {
        classes() {
            let res = ['jc-inp']
            return res
        },
    },
}
</script>
