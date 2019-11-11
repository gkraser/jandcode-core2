<template>
    <App :app="this">
    </App>
</template>

<script>
    import App from "./JcBaseAppStd-App"

    export default {
        components: {
            App: App
        },
        data() {
            return {
                title: 'Без заголовка',
                icon: 'app-logo',
                left: false,
                leftWidth: 280,
                right: false,
                rightWidth: 280,
            }
        },
        methods: {
            home() {
            }
        }
    }
</script>
