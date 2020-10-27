<template>
    <div>
        <span>font-size:</span>
        <input type="range" :min="fontSizeMin" :max="fontSizeMax"
               v-model.number="fontSize">
        <span>{{ fontSize }}px</span>
        <button @click="changeFontSize(-1)">-</button>
        <button @click="changeFontSize(1)">+</button>
        <button @click="fontSize=13">13</button>
        <button @click="fontSize=14">14</button>
        <button @click="fontSize=16">16</button>
        <button @click="fontSize=24">24</button>
    </div>
</template>

<script>
import {jsaBase} from '../vendor'

export default {
    name: "tst-fontsize",
    props: {},
    data() {
        return {
            fontSizeMin: 8,
            fontSizeMax: 38,
            fontSize: 13,
        }
    },
    created() {
        let fs = jsaBase.toInt(window.getComputedStyle(document.documentElement).fontSize)
        if (fs) {
            this.fontSize = fs
        }
    },
    watch: {
        fontSize: function(v) {
            document.documentElement.style.fontSize = '' + this.fontSize + 'px';
        },
    },
    methods: {
        changeFontSize(inc) {
            let v = this.fontSize + inc
            if (v >= this.fontSizeMin && v <= this.fontSizeMax) {
                this.fontSize = v;
            }
        },
    },
}
</script>
