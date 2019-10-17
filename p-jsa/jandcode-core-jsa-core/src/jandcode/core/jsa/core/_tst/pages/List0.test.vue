<template>
    <tst-panel class="list1-test-5c1e7041">

        <div class="row">
            <jc-list hide-toggle>
                <jc-list-item>
                    <template #content>
                        <div>Пункт 1 обычный</div>
                    </template>
                </jc-list-item>
                <jc-list-item>
                    <template #content>
                        <div>Пункт 2</div>
                    </template>
                    <template #nested>
                        <div>Вложенный 2</div>
                    </template>
                </jc-list-item>
                <jc-list-item>
                    <template #content>
                        <div>Пункт 3</div>
                    </template>
                    <template #nested>
                        <div>Вложенный 3</div>
                    </template>
                </jc-list-item>
                <jc-list-item>
                    <template #content>
                        <div>Пункт 4 обычный</div>
                    </template>
                </jc-list-item>
                <jc-list-item>
                    <template #content>
                        <jc-action text="Hello" icon="bus"/>
                    </template>
                </jc-list-item>
                <jc-list-item>
                    <template #content>
                        <jc-action text="Hello" icon="bus">
                            <jc-action text="Hello" icon="bus"/>
                            <jc-action text="Hello" icon="bus"/>
                            <jc-action text="Hello" icon="bus"/>
                            <jc-action text="Hello" icon="bus"/>
                        </jc-action>
                    </template>
                </jc-list-item>
            </jc-list>
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

    .list1-test-5c1e7041 {

        .row {
            display: flex;
        }

        .row > * {
            margin-right: 20px;
            margin-bottom: 10px;
        }

        .jc-list {
            border: 1px solid green
        }

    }

</style>
