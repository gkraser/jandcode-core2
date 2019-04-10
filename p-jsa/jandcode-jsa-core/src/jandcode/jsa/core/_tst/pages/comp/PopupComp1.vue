<template>
    <div class="popupcomp1-210d3a19" :class="cls1()">

        POPUP: {{nm}}

        <button @click="show1">show1</button>
        <button @click="show3">show3</button>
        <button @click="info1">info1</button>
        <button @click="close1">close1</button>
        <button @click="showModal1">showModal1</button>

    </div>
</template>

<script>
    import {jsaBase, Vue} from '../vendor'

    let popupManager = jsaBase.popupManager

    let cnt = 0

    let Comp = {
        props: {
            inlineComp: {
                default: false
            },
            nm: {
                default: function() {
                    cnt++
                    return 'p-' + cnt
                }
            },
            popupObject: Object
        },
        data() {
            return {}
        },
        methods: {

            cls1() {
                let cls = []
                if (this.inlineComp) {
                    cls.push('inline')
                }
                if (this.popupObject) {
                    if (this.popupObject.modal) {
                        cls.push('modal')
                    }
                }
                return cls
            },

            doShow(modal) {
                let popup = popupManager.createPopup()
                let ConstComp = Vue.extend(Comp)
                let vm = new ConstComp({
                    propsData: {
                        popupObject: popup
                    }
                })
                vm.$mount()
                popup.popupEl = vm.$el
                let nb = jsaBase.dom.getNodeBound(this.$el)
                popup.params = {
                    type: 'contextmenu',
                    x: nb.x + 40,
                    y: nb.y + 20,
                }
                popup.modal = modal
                popupManager.showPopup(popup)
                return vm;
            },

            show1() {
                this.doShow(false)
            },

            showModal1() {
                this.doShow(true)
            },

            info1() {
                console.info("popupManager");
                for (let z of popupManager.stack) {
                    console.info(z.popupEl);
                }
            },

            show3() {
                let cur = this;
                for (let i = 0; i < 3; i++) {
                    cur = cur.doShow()
                }
            },

            close1() {
                if (!this.popupObject) return;
                popupManager.closePopup(this.popupObject)
            },

        }
    }

    export default Comp
</script>

<style lang="less">

    .popupcomp1-210d3a19.inline {
        background-color: white;
    }

    .popupcomp1-210d3a19.modal {
        background-color: #f8bbd0;
    }

    .popupcomp1-210d3a19 {

        display: inline-block;
        border: 1px solid #0b97c4;
        padding: 4px;
        background-color: #b9f6ca;
    }

</style>
