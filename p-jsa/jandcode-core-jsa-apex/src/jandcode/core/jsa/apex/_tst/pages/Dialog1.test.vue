<template>
    <div>
        dialogs
        <q-btn label="show1" @click="show1"/>
        <q-btn label="DialogOnXxx1" @click="show2"/>
    </div>
</template>

<script>
    import {apex, dao} from './vendor'

    import Dialog1 from './Dialog1'
    import DialogOnXxx1 from './DialogOnXxx1'

    function daoFake(ctx) {
        setTimeout(function() {
            let res = {
                a: 1, b: 2
            }
            ctx.resolve(res)
        }, 2000)
    }


    export default {
        components: {
            Dialog1,
            DialogOnXxx1,
        },
        props: {},
        data() {
            return {}
        },
        mounted() {
            this.show1()
        },
        methods: {
            show1() {
                apex.showDialog({frame: Dialog1})
            },
            show2() {
                apex.showDialog({
                    frame: DialogOnXxx1,
                    onCancel1: function() {
                        console.info("out CCCCC");
                        //return false
                    },
                    onOk: async function(frame) {
                        console.info("OK!", frame);
                        //return false

                        let res = await dao.invoke(daoFake)

                        if (res.a === 1) {
                            console.warn("ERROR ok");
                            return false
                        }

                    }
                })
            }
        }
    }
</script>

<style lang="scss">
    .jc-frame--footer {
        //padding: 40px;

        &.q-gutter-x-sm > *, &.q-gutter-sm > * {
            //margin-left: 68px;
        }

    }
</style>
