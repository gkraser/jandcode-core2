<template>
    <div class="tst-apx-panel">
        <div class="tst-apx-panel--head">
            <template v-if="cfgTst.path">
                <div class="tst-apx-panel--value-big">{{ cfgTst.fileName }}</div>
                <span class="tst-apx-panel--divider"></span>
                <div class="tst-apx-panel--value-small"><a
                        :href="'?path='+cfgTst.filePath">{{ cfgTst.filePath }}/</a>
                </div>
                <span class="tst-apx-panel--divider" :style="{flex:1}"></span>
                <div class="tst-apx-panel--value-small">Theme:</div>
                <select v-model="curTheme">
                    <option v-for="theme in cfgTst.themes">{{ theme.path }}</option>
                </select>
                <button @click="resetTheme">Reset theme</button>
                <button v-for="n in cfgTst.themeNamesSwitch" @click="switchTheme(n)">
                    &#8594;
                    {{ n }}
                </button>
            </template>
        </div>

        <div class="tst-apx-panel--head">
            <tst-btn @click="resetCfg" label="resetCfg"/>
            <tst-checkbox label="debugBg" v-model="cfg.debugBg" v-if="debugBg"/>
            <tst-fontsize v-if="fontsize"/>
            <slot name="tools"/>
            <portal-target name="tools" multiple></portal-target>
        </div>

        <div class="tst-apx-panel--head" v-if="$slots['tools-1']">
            <slot name="tools-1"/>
        </div>

        <div class="tst-apx-panel--head" v-if="$slots['tools-2']">
            <slot name="tools-2"/>
        </div>

        <div class="tst-apx-panel--body" ref="body"
             :class="[noPadding?'tst-apx-panel--no-padding':'', bodyClass]"
             :style="bodyStyle">
            <slot></slot>
        </div>
    </div>
</template>

<script>
import {jsaBase} from '../vendor'
import * as mixins from '../mixins'

export default {
    name: 'tst-apx-panel',
    mixins: [mixins.cfgStore],
    props: {
        debugBg: {
            type: Boolean,
            default: null
        },
        fontsize: {
            type: Boolean,
            default: null
        },
        noPadding: {
            type: Boolean,
            default: null
        },
        bodyClass: {
            default: ''
        },
        bodyStyle: {
            default: ''
        }
    },
    data() {
        return {
            curTheme: Jc.cfg.tst.theme,
        }
    },
    created() {
        this.cfgStore.applyDefault({
            debugBg: false,
        })
    },
    watch: {
        curTheme: function(v) {
            let prms = jsaBase.url.getPageParams()
            prms.theme = v
            window.location.search = decodeURIComponent(jsaBase.url.params(prms))
        }
    },
    computed: {
        cfgTst() {
            return Jc.cfg.tst || {}
        },

    },
    methods: {
        resetTheme() {
            let prms = jsaBase.url.getPageParams()
            delete prms.theme
            window.location.search = decodeURIComponent(jsaBase.url.params(prms))
        },
        switchTheme(theme) {
            let prms = jsaBase.url.getPageParams()
            prms.theme = theme
            window.location.search = decodeURIComponent(jsaBase.url.params(prms))
        },
        resetCfg() {
            return this.cfgStore.reset();
        },
        applyCfg() {
            document.body.classList.toggle("debug-bg", this.cfg.debugBg)
        }
    }
}
</script>

<style>

.tst-apx-panel {
  min-height: 100vh;
}

.tst-apx-panel--head {
  font-family: Arial, Helvetica, sans-serif;
  font-size: 13px;
  border-bottom: 1px solid silver;
  background-color: #f5f5f5;
  padding: 4px 10px;
  display: flex;
  min-height: 34px;
  align-items: center;
  flex-wrap: wrap;
}

.tst-apx-panel--head > *:not(:first-child) {
  margin-left: 8px;
}

.tst-apx-panel--head input[type="range"] {
  color: red;
  width: 60px;
}

.tst-apx-panel--head .tst-apx-panel--value-big {
  color: navy;
  font-size: 12px;
  font-family: "Lucida Console", Monaco, monospace;
}

.tst-apx-panel--head .tst-apx-panel--value-small {
  font-size: 11px;
  color: gray;
  font-family: "Lucida Console", Monaco, monospace;
}

.tst-apx-panel--head select {
  color: navy;
  font-size: 11px;
  font-family: "Lucida Console", Monaco, monospace;
  padding: 2px;
  background-color: #f5f5f5;
}

.tst-apx-panel--head button {
  font-size: 11px;
  white-space: nowrap;
}

.tst-apx-panel--head a:hover {
  text-decoration: underline;
}

.tst-apx-panel--body {
  padding: 20px 20px;
}

.tst-apx-panel--body.tst-apx-panel--no-padding {
  padding: 0;
}

.tst-apx-panel--size-label {
  min-width: 30px;
}

.tst-apx-panel--divider {
  width: 4px;
}

.tst-apx-panel--head .vue-portal-target {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}

.tst-apx-panel--head .vue-portal-target > *:not(:first-child) {
  margin-left: 8px;
}

</style>