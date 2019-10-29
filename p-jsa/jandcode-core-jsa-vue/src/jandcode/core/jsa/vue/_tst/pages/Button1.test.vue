<template>
    <tst-panel class="button1-test-0abb40eb">

        <div v-for="t in types()" :class="'row'">

            <jc-button :type="t" :text="txt()"/>
            <jc-button :type="t" :text="txt()" icon="bus"/>
            <jc-button :type="t" icon="bus"/>

            <jc-button :type="t" :text="txt()"/>
            <jc-button :type="t" :text="txt()" icon="bus"/>
            <jc-button :type="t" icon="bus"/>

            <jc-button :type="t" :text="txt()">
                <TestMenu1/>
            </jc-button>

            <jc-button :type="t" :text="txt()" icon="bus">
                <TestMenu1/>
            </jc-button>

            <div>{{t}}</div>
        </div>

        <div>Toolbar</div>
        <jc-toolbar>
            <jc-action text="Action1" icon="bus"/>
            <jc-action text="Action1"/>

            <jc-button text="Нажми" icon="bus"/>
            <jc-button icon="bus"/>
        </jc-toolbar>

    </tst-panel>

</template>

<script>
    import TestMenu1 from './comp/TestMenu1'
    import * as b from '../../js/comp/action/JcButton'

    export default {
        components: {
            TestMenu1,
        },
        props: {},
        data() {
            return {}
        },
        methods: {
            types() {
                let res = [null]
                for (let t in b.config.type) {
                    res.push(t)
                }
                console.info("res", res);
                return res
            },
            txt() {
                return "Нажми"
            }
        }

    }
</script>

<style lang="less">


    .button1-test-0abb40eb {

        .row {
            margin-bottom: 10px;
            display: flex;

            & > * {
                margin-right: 5px;
            }

        }

        .row-2 {

            background-color: rgba(255, 0, 0, 0.31);

            .jc-button {
                background-color: #c1f26a;

            }

        }

        .row-2 .jc-button > * {
            background-color: #9ccc65 !important;
        }

    }
</style>