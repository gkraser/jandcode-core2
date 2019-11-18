<template>
    <Dialog>
        Hello
    </Dialog>
</template>

<script>
    import {apex, dao} from './vendor'

    function daoFake(ctx) {
        setTimeout(function() {
            let res = {
                a: 1, b: 2
            }
            ctx.resolve(res)
        }, 20)
    }

    function daoFakeErr(ctx) {
        setTimeout(function() {
            throw new Error("Error in daoFakeErr")
        }, 20)
    }

    export default {
        extends: apex.JcFrame,

        props: {},
        data() {
            return {}
        },
        methods: {
            onOk1() {
                console.info("onOk in dialog");
                //return false
                return dao.invoke(daoFake)
                    .then((ctx) => {
                        console.info("then in onOk", ctx);
                        //return false
                    })
            },
            async onOk() {
                console.info("async onOk in dialog");
                let a = await dao.invoke(daoFakeErr)
                console.info("async then in onOk", a);
            },
            onCmd1(cmd) {
                console.info("onCmd in dialog", cmd);
                //return false
                return dao.invoke(daoFake)
                    .then((ctx) => {
                        console.info("then in onOk", ctx);
                        //return false
                    })
            }
        },
    }
</script>
