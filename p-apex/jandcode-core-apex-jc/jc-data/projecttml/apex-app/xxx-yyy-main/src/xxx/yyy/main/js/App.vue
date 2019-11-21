<template>
    <App>

        <template #title>
        </template>

        <template #toolbar>
        </template>

        <template #left>
        </template>

        <template #main>
            <router-view></router-view>
        </template>

    </App>
</template>

<script>
    import {apex} from './vendor'

    export default {
        extends: apex.JcApp,
        components: {},
        props: {},
        created() {
            this.title = "XxxYyy";
        },
        data() {
            return {}
        },
        methods: {}
    }
</script>
