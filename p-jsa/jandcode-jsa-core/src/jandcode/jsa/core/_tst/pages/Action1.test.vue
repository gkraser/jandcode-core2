<template>
    <tst-panel class="action1-59f8b2b5">

        <div v-for="n in 2" :class="'row row-'+n">
            <jc-action text="Hello"/>
            <jc-action text="Hello" icon="bus" caret-icon="caret-down"/>
            <jc-action text="Hello" icon="bus"/>
            <jc-action text="Hello" caret-icon="caret-down"/>
            <jc-action text="Hello" caret-icon="caret-right"/>
            <jc-action text="Hello-e" caret-icon="caret-down" icon="empty"/>
            <jc-action text="Hello-e" caret-icon="caret-right" icon="empty"/>
            <jc-action icon="bus" caret-icon="caret-down"/>
            <jc-action icon="bus" caret-icon="caret-right"/>
            <jc-action icon="bus"/>
            <jc-action text="Hello"/>
            <jc-action text="Hello"/>
        </div>

    </tst-panel>

</template>

<script>

    export default {
        props: {},
        data() {
            return {}
        },
        methods: {}

    }
</script>

<style lang="less">

    .action1-59f8b2b5 {

        .row {
            margin-bottom: 10px;

            & > * {
                margin-right: 5px;
            }
        }

        .row-2 {

            background-color: red;

            .jc-action {
                background-color: #c1f26a;

            }

        }

        .row-2 .jc-action > * {
            background-color: #9ccc65 !important;
        }

    }
</style>