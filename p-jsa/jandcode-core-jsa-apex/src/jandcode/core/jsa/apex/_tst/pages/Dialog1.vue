<template>
    <Dialog :size="size" buttons="ync">
        Это диалог.
        <q-btn label="Другой" no-caps @click="show1"/>
        <q-btn label="cnt" no-caps @click="cnt==1?cnt=20:cnt=1"/>
        <q-btn label="size:max" no-caps @click="size='max'"/>
        <q-btn label="size:norm" no-caps @click="size=''"/>

        <div :style="{width1:''+(size)+'px'}">
            Место

            <q-banner class="bg-primary text-white" style1="width:1800px">
                Unfortunately,
                <template v-slot:action>
                    <q-btn flat color="white" label="Dismiss"/>
                    <q-btn flat color="white" label="Update Credit Card"/>
                </template>
            </q-banner>

            <div v-for="n in cnt" style="padding:20px">
                {{n}}
            </div>

        </div>
    </Dialog>
</template>

<script>
    import {apex} from './vendor'

    let mixinDecor = {
        mixins: [apex.JcFrame],
        components: {
            //Dialog: Dialog1Decor
        },
    }

    let Comp = {
        //extends: apex.JcFrame,
        mixins: [mixinDecor],
        props: {
            size: {
                type: String,
                default: ''
            }
        },
        created() {
            this.title = "Заголовок диалога"
            this.icon = "frame"
        },
        destroyed() {
            console.info("Frame destroyed");
        },
        data() {
            return {
                cnt: 1
            }
        },
        methods: {
            show1() {
                console.info("this.$options", this.$options);
                console.info("Cmp", Comp);
                apex.showDialog({frame:Comp, propsData: {size: this.size - 100}})
            }
        }
    }

    export default Comp
</script>
