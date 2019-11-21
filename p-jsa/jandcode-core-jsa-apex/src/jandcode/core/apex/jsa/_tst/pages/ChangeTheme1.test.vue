<template>
    <App>
        <template #main>
            <div class="q-pa-md">
                <q-btn v-for="t in $jc.cfg.tst.themes" :label="t.name"
                       @click="changeTheme(t.name)"/>
            </div>
        </template>
    </App>
</template>

<script>
    import {apex} from './vendor'


    export default {
        extends: apex.JcApp,
        props: {},
        data() {
            return {}
        },
        created() {
            Jc.loadModule("[*]/css/*-theme.js")
        },
        methods: {
            changeTheme(name) {
                let theme = Jc.cfg.tst.themes[name]
                apex.theme.applyTheme(theme.path)
            }
        }
    }
</script>
