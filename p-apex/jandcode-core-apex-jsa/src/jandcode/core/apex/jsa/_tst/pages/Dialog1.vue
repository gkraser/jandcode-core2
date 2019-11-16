<template>
    <Dialog>
        Это диалог.
        <q-btn label="Другой" @click="show1"/>
        <q-btn label="cnt" @click="cnt==1?cnt=20:cnt=1"/>
        
        <div :style="{width:''+size+'px'}">
            Место
            <div v-for="n in cnt" style="padding:20px">{{n}}</div>

        </div>
    </Dialog>
</template>

<script>
    import {apex} from './vendor'
    import Dialog1Decor1 from './Dialog1Decor1'

    let mixinDecor = {
        mixins: [apex.JcFrame],
        components: {
            Dialog: Dialog1Decor1
        },
    }

    let Comp = {
        //extends: apex.JcFrame,
        mixins: [mixinDecor],
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
            return {
                cnt: 20
            }
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
