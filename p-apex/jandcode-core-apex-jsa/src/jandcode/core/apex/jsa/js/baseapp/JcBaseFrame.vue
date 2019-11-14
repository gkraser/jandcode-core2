<template>
    <Page>
    </Page>
</template>

<script>
    import Page from "./JcBaseFrame-Page"
    import Dialog from "./JcBaseFrame-Dialog"

    export default {
        components: {
            Page: Page,
            Dialog: Dialog,
        },
        data() {
            return {
                title: 'Без заголовка',
                title2: '',
                icon: 'frame',
            }
        },
        methods: {}
    }
</script>
