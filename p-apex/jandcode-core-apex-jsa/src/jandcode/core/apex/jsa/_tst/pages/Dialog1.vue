<template>
    <Dialog>
        Это диалог.
        <q-btn label="Другой" @click="show1"/>

        <div :style="{width:''+size+'px'}">
            Место
            <div v-for="n in 10" style="padding:20px">{{n}}</div>

        </div>
    </Dialog>
</template>

<script>
    import {apex} from './vendor'

    let Comp = {
        extends: apex.JcFrame,
        props: {
            size: {
                type: Number,
                default: 500
            }
        },
        destroyed() {
            console.info("Frame destroyed");
        },
        data() {
            return {}
        },
        methods: {
            show1() {
                console.info("this.$options", this.$options);
                console.info("Cmp", Comp);
                apex.frame.showDialog(Comp, {props: {size: this.size - 100}})
            }
        }
    }

    export default Comp
</script>
