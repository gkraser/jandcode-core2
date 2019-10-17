<template>
    <div class="comp">
        <form>
            <fieldset>
                <legend>Настройка popup</legend>
                <label>Тип выравнивания</label>
                <select v-model="alignType">
                    <option v-for="a in alignTypes">{{a}}</option>
                </select>
                <label>Размер popup</label>
                <select v-model="popupSizeIndex">
                    <option v-for="a in popupSizes" :value="a.index">{{a.text}}</option>
                </select>
                <label>Размер control</label>
                <select v-model="controlSizeIndex">
                    <option v-for="a in controlSizes" :value="a.index">{{a.text}}</option>
                </select>
                <div>
                    ctrl+click для перемещения control в другое место
                </div>
            </fieldset>
        </form>

        <div ref="control" class="control">
            <span>Это control</span>
            <span ref="controlSize">0</span>
        </div>
        <div ref="popup" class="popup">
            <span>Это popup</span>
            <span ref="popupSize">0</span>
        </div>
    </div>
</template>

<script>

    import {jsaBase} from './vendor'

    let popupSizes = [
        {w: 100, h: 100},
        {w: 200, h: 200},
        {w: 300, h: 300},
        {w: 600, h: 600},
        {w: 2000, h: 2000},
    ]

    let controlSizes = [
        {w: 150, h: 30},
        {w: 250, h: 30},
        {w: 350, h: 30},
    ]

    export default {

        data() {
            return {
                alignType: 'dropdown',
                popupSizeIndex: 0,
                controlSizeIndex: 0,
            }
        },

        watch: {
            alignType: 'doAlign',
            popupSizeIndex: 'doAlign',
            controlSizeIndex: 'doAlign',
        },

        computed: {
            alignTypes: function() {
                return Object.keys(jsaBase.popup.alignPopupTypes)
            },
            popupSizes: function() {
                return popupSizes.map((item, idx) => {
                    return {text: '' + item.w + 'x' + item.h, index: idx}
                })
            },
            controlSizes: function() {
                return controlSizes.map((item, idx) => {
                    return {text: '' + item.w + 'x' + item.h, index: idx}
                })
            }
        },

        mounted: function() {
            let th = this

            window.addEventListener('click', function(ev) {
                if (ev.ctrlKey) {
                    let nodeOwn = th.$refs.control
                    nodeOwn.style.left = jsaBase.dom.toStyleSize(ev.clientX)
                    nodeOwn.style.top = jsaBase.dom.toStyleSize(ev.clientY)
                    th.doAlign()
                    ev.preventDefault()
                    ev.stopPropagation()
                }
            }, true);

            this.doAlign()
        },

        methods: {
            doAlign() {
                let nodeOwn = this.$refs.control
                let nodePop = this.$refs.popup
                let params = {
                    type: this.alignType
                }

                // fix control должен быть на экране
                let ownBound = jsaBase.dom.getNodeBound(nodeOwn)
                let vpBound = jsaBase.dom.getViewportBound()
                if (ownBound.x + ownBound.w > vpBound.w) {
                    ownBound.x = vpBound.w - ownBound.w
                }
                if (ownBound.y + ownBound.h > vpBound.h) {
                    ownBound.y = vpBound.h - ownBound.h
                }
                jsaBase.dom.setNodeBound(nodeOwn, ownBound)


                let sz = popupSizes[this.popupSizeIndex]
                nodePop.style.width = jsaBase.dom.toStyleSize(sz.w)
                nodePop.style.height = jsaBase.dom.toStyleSize(sz.h)
                nodePop.style.display = 'block'

                sz = controlSizes[this.controlSizeIndex]
                nodeOwn.style.width = jsaBase.dom.toStyleSize(sz.w)
                nodeOwn.style.height = jsaBase.dom.toStyleSize(sz.h)
                nodeOwn.style.display = 'block'

                jsaBase.popup.alignPopup(nodeOwn, nodePop, params)

                let nodeSize = this.$refs.popupSize
                sz = jsaBase.dom.getNodeBound(nodePop)
                nodeSize.innerHTML = `${sz.w}x${sz.h}`

                nodeSize = this.$refs.controlSize
                sz = jsaBase.dom.getNodeBound(nodeOwn)
                nodeSize.innerHTML = `${sz.w}x${sz.h}`

            }
        }

    }

</script>

<style>

    .comp {
        box-sizing: border-box;
    }

    .control {
        left: 300px;
        top: 150px;
        position: fixed;
        width: 150px;
        height: 30px;
        border: 1px solid red;
        background: #d7ffb8;
    }

    .popup {
        left: 100px;
        top: 100px;
        position: fixed;
        width: 100px;
        height: 100px;
        border: 1px solid green;
        background: #c8e1ff;
        display: none;
    }

</style>