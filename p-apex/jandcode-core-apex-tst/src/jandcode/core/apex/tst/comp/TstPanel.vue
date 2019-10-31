<template>
    <div class="tst-panel">
        <div class="tst-panel--head">
            <template v-if="cfg.path">
                <div class="tst-panel--value-big">{{cfg.fileName}}</div>
                <span class="tst-panel--divider"></span>
                <div class="tst-panel--value-small"><a :href="'?path='+cfg.filePath">{{cfg.filePath}}/</a>
                </div>
                <span class="tst-panel--divider" :style="{flex:1}"></span>
                <div class="tst-panel--value-small">Theme:</div>
                <select v-model="curTheme">
                    <option v-for="theme in cfg.themes">{{theme.path}}</option>
                </select>
                <button @click="resetTheme">Reset theme</button>
            </template>
        </div>
        <div class="tst-panel--head">
            <span>font-size:</span>
            <input type="range" :min="fontSizeMin" :max="fontSizeMax"
                   v-model.number="fontSize">
            <span class="tst-panel--size-label">{{fontSize}}px</span>
            <button @click="changeFontSize(-1)">-</button>
            <button @click="changeFontSize(1)">+</button>
            <button @click="fontSize=13">13</button>
            <button @click="fontSize=14">14</button>
            <button @click="fontSize=16">16</button>
            <button @click="fontSize=24">24</button>
            <span class="tst-panel--divider"></span>
            <slot name="tools"></slot>
        </div>
        <div class="tst-panel--body">
            <slot></slot>
        </div>
    </div>
</template>

<script>
    import * as jsaBase from 'jandcode.core.jsa.base'

    export default {
        name: 'tst-panel',
        props: {},
        data() {
            return {
                fontSizeMin: 8,
                fontSizeMax: 38,
                fontSize: 13,
                curTheme: Jc.cfg.tst.theme,
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
            curTheme: function(v) {
                let prms = jsaBase.url.getPageParams()
                prms.theme = v
                window.location.search = jsaBase.url.params(prms)
            }
        },
        computed: {
            cfg() {
                return Jc.cfg.tst || {}
            }
        },
        methods: {
            changeFontSize(inc) {
                let v = this.fontSize + inc
                if (v >= this.fontSizeMin && v <= this.fontSizeMax) {
                    this.fontSize = v;
                }
            },

            resetTheme() {
                let prms = jsaBase.url.getPageParams()
                delete prms.theme
                window.location.search = jsaBase.url.params(prms)
            }
        }
    }
</script>

<style>

    .tst-panel {
        min-height: 100vh;
    }

    .tst-panel--head {
        font-family: Arial, Helvetica, sans-serif;
        font-size: 13px;
        border-bottom: 1px solid silver;
        background-color: #f5f5f5;
        padding-left: 10px;
        padding-right: 10px;
        display: flex;
        height: 34px;
        align-items: center;
    }

    .tst-panel--head > *:not(:first-child) {
        margin-left: 4px;
    }

    .tst-panel--head input[type="range"] {
        color: red;
        width: 60px;
    }

    .tst-panel--head .tst-panel--value-big {
        color: navy;
        font-size: 12px;
        font-family: "Lucida Console", Monaco, monospace;
    }

    .tst-panel--head .tst-panel--value-small {
        font-size: 11px;
        color: gray;
        font-family: "Lucida Console", Monaco, monospace;
    }

    .tst-panel--head select {
        color: navy;
        font-size: 12px;
        font-family: "Lucida Console", Monaco, monospace;
        padding: 2px;
        background-color: #f5f5f5;
    }

    .tst-panel--head a:hover {
        text-decoration: underline;
    }

    .tst-panel--body {
        padding: 20px 20px;
    }

    .tst-panel--size-label {
        min-width: 30px;
    }

    .tst-panel--divider {
        width: 10px;
    }

</style>