<template>
    <tst-panel class="list1-test-5c1e7041">

        <div class="row">
            <template v-for="n in 2">
                <jc-list>
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
                </jc-list>
            </template>
        </div>

        <div class="row">
            <template v-for="n in 2">
                <jc-list>
                    <jc-list-item>
                        <template #content>
                            <div>Пункт 1 обычный</div>
                        </template>
                    </jc-list-item>
                    <jc-list-item>
                        <template #content>
                            <div>Пункт 4 обычный</div>
                        </template>
                    </jc-list-item>
                    <jc-list-item>
                        <template #content>
                            <div>Пункт 5 обычный</div>
                        </template>
                    </jc-list-item>
                </jc-list>
            </template>
        </div>

        <div class="row">
            <template v-for="n in 2">
                <jc-list>
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
                            <div>Пункт 4</div>
                        </template>
                        <template #nested>
                            <div>Вложенный 4</div>
                        </template>
                    </jc-list-item>
                </jc-list>
            </template>
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
